package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.AccountRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.LoginRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountService {

    Account createAccount(AccountRequestDTO account);

    Account updateAccount(Integer customerId, Account account);

    void deleteAccount(Integer customerId);

    Optional<Account> getAccountById(Integer customerId);

    Optional<Account> getAccountByEmail(String email);

    List<Account> getAllAccounts();

    boolean existsByEmail(String email);

    Map<String, Object> loginAccount(LoginRequestDTO loginRequestDTO);

    // Email verification
    void verifyEmail(String token);

    void resendVerificationEmail(String email);

    // Token management
    Map<String, Object> refreshAccessToken(String refreshToken);

    void logout(String accessToken, String refreshToken, long tokenRemainingMillis);
}
