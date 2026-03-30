package com.example.PixelMageEcomerceProject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.entity.UnlinkRequest;
import com.example.PixelMageEcomerceProject.enums.CardProductStatus;
import com.example.PixelMageEcomerceProject.enums.UnlinkRequestStatus;
import com.example.PixelMageEcomerceProject.exceptions.TokenExpiredException;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.CardRepository;
import com.example.PixelMageEcomerceProject.repository.UnlinkRequestRepository;
import com.example.PixelMageEcomerceProject.service.impl.UnlinkRequestServiceImpl;
import com.example.PixelMageEcomerceProject.service.interfaces.NFCScanService;

@ExtendWith(MockitoExtension.class)
class UnlinkRequestServiceTest {

    @Mock
    private UnlinkRequestRepository unlinkRequestRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private NFCScanService nfcScanService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UnlinkRequestServiceImpl unlinkRequestService;

    // ── Test 1: Customer creates request → email sent (Done Condition: Customer tạo request → email sent) ──

    @Test
    void createRequest_success_emailSent() {
        Account customer = buildAccount(1, "customer@test.com", "Minh");
        Card card = buildLinkedCard("NFC-001", customer);

        when(accountRepository.findById(1)).thenReturn(Optional.of(customer));
        when(cardRepository.findByNfcUid("NFC-001")).thenReturn(Optional.of(card));
        when(unlinkRequestRepository.save(any(UnlinkRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        unlinkRequestService.createRequest(1, "NFC-001");

        // Verify email was sent
        verify(emailService).sendUnlinkVerificationEmail(
                eq("customer@test.com"), eq("Minh"), anyString());

        // Verify request was persisted with correct initial state
        ArgumentCaptor<UnlinkRequest> captor = ArgumentCaptor.forClass(UnlinkRequest.class);
        verify(unlinkRequestRepository).save(captor.capture());
        UnlinkRequest saved = captor.getValue();
        assertThat(saved.getStatus()).isEqualTo(UnlinkRequestStatus.PENDING_EMAIL);
        assertThat(saved.getToken()).isNotBlank();
        assertThat(saved.getTokenExpiry()).isAfter(LocalDateTime.now());
    }

    // ── Test 2: Token click → status = EMAIL_CONFIRMED (Done Condition: Token click → EMAIL_CONFIRMED) ──

    @Test
    void verifyToken_success_statusEmailConfirmed() {
        UnlinkRequest request = buildRequest(UnlinkRequestStatus.PENDING_EMAIL,
                LocalDateTime.now().plusMinutes(5)); // still valid

        when(unlinkRequestRepository.findByToken("valid-token")).thenReturn(Optional.of(request));
        when(unlinkRequestRepository.save(any(UnlinkRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        unlinkRequestService.verifyToken("valid-token");

        assertThat(request.getStatus()).isEqualTo(UnlinkRequestStatus.EMAIL_CONFIRMED);
        verify(unlinkRequestRepository).save(request);
    }

    // ── Test 3: Expired token → TokenExpiredException → 410 (Done Condition: Expired token → 410) ──

    @Test
    void verifyToken_expired_throwsTokenExpiredException() {
        UnlinkRequest request = buildRequest(UnlinkRequestStatus.PENDING_EMAIL,
                LocalDateTime.now().minusMinutes(15)); // expired

        when(unlinkRequestRepository.findByToken("expired-token")).thenReturn(Optional.of(request));

        assertThrows(TokenExpiredException.class,
                () -> unlinkRequestService.verifyToken("expired-token"));

        // Status must NOT change
        assertThat(request.getStatus()).isEqualTo(UnlinkRequestStatus.PENDING_EMAIL);
        verify(unlinkRequestRepository, never()).save(any());
    }

    // ── Test 4: Staff approve → unlinkCard() chain executed (Done Condition: Staff approve → full chain) ──

    @Test
    void approve_success_unlinkChainExecuted() {
        Account customer = buildAccount(1, "customer@test.com", "Minh");
        Card card = buildLinkedCard("NFC-001", customer);
        UnlinkRequest request = buildRequest(UnlinkRequestStatus.EMAIL_CONFIRMED,
                LocalDateTime.now().plusMinutes(5));
        request.setCustomer(customer);
        request.setNfcUid("NFC-001");

        when(unlinkRequestRepository.findById(42L)).thenReturn(Optional.of(request));
        when(cardRepository.findByNfcUid("NFC-001")).thenReturn(Optional.of(card));
        when(unlinkRequestRepository.save(any(UnlinkRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        unlinkRequestService.approve(42L);

        // Full chain must fire
        verify(nfcScanService).unlinkCard("NFC-001", 1);

        // Status updated
        assertThat(request.getStatus()).isEqualTo(UnlinkRequestStatus.APPROVED);
        assertThat(request.getResolvedAt()).isNotNull();

        // Customer notified
        verify(emailService).sendUnlinkApprovedEmail("customer@test.com", "Minh", "NFC-001");
    }

    // ── Test 5: Staff reject → status REJECTED + customer notified (Done Condition: Staff reject → notify) ──

    @Test
    void reject_success_customerNotified() {
        Account customer = buildAccount(1, "customer@test.com", "Minh");
        UnlinkRequest request = buildRequest(UnlinkRequestStatus.EMAIL_CONFIRMED,
                LocalDateTime.now().plusMinutes(5));
        request.setCustomer(customer);
        request.setNfcUid("NFC-001");

        when(unlinkRequestRepository.findById(99L)).thenReturn(Optional.of(request));
        when(unlinkRequestRepository.save(any(UnlinkRequest.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        unlinkRequestService.reject(99L, "Thẻ không hợp lệ");

        assertThat(request.getStatus()).isEqualTo(UnlinkRequestStatus.REJECTED);
        assertThat(request.getStaffNote()).isEqualTo("Thẻ không hợp lệ");
        assertThat(request.getResolvedAt()).isNotNull();

        verify(emailService).sendUnlinkRejectedEmail("customer@test.com", "Minh", "Thẻ không hợp lệ");
        // unlinkCard must NOT be called on reject
        verify(nfcScanService, never()).unlinkCard(anyString(), anyInt());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Account buildAccount(Integer id, String email, String name) {
        Account a = new Account();
        a.setCustomerId(id);
        a.setEmail(email);
        a.setName(name);
        return a;
    }

    private Card buildLinkedCard(String nfcUid, Account owner) {
        Card c = new Card();
        c.setStatus(CardProductStatus.LINKED);
        c.setOwner(owner);
        return c;
    }

    private UnlinkRequest buildRequest(UnlinkRequestStatus status, LocalDateTime tokenExpiry) {
        UnlinkRequest r = new UnlinkRequest();
        r.setId(1L);
        r.setStatus(status);
        r.setToken("valid-token");
        r.setTokenExpiry(tokenExpiry);
        return r;
    }
}
