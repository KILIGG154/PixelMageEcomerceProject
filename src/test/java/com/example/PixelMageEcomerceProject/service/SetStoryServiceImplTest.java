package com.example.PixelMageEcomerceProject.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.PixelMageEcomerceProject.dto.response.SetStoryResponse;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.entity.SetStory;
import com.example.PixelMageEcomerceProject.entity.UserInventory;
import com.example.PixelMageEcomerceProject.entity.UserStoryUnlock;
import com.example.PixelMageEcomerceProject.exceptions.StoryNotUnlockedException;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.SetStoryRepository;
import com.example.PixelMageEcomerceProject.repository.UserInventoryRepository;
import com.example.PixelMageEcomerceProject.repository.UserStoryUnlockRepository;
import com.example.PixelMageEcomerceProject.service.impl.SetStoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class SetStoryServiceImplTest {

    @Mock
    private SetStoryRepository setStoryRepository;
    @Mock
    private UserStoryUnlockRepository userStoryUnlockRepository;
    @Mock
    private UserInventoryRepository userInventoryRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private SetStoryServiceImpl setStoryService;

    @Test
    void revokeStoriesIfConditionNotMet_softRevokes_neverDeletes() {
        // Arrange
        Integer userId = 1;
        Account user = new Account();
        user.setCustomerId(userId);

        SetStory story = new SetStory();
        story.setStoryId(10);
        story.setRequiredTemplateIds("[1,2]");

        UserStoryUnlock unlock = new UserStoryUnlock();
        unlock.setUnlockId(100);
        unlock.setUser(user);
        unlock.setStory(story);
        unlock.setIsActive(true);

        // User currently only has template 1, meaning they lost template 2 (e.g. unlinked card)
        UserInventory inv = new UserInventory();
        CardTemplate ct = new CardTemplate();
        ct.setCardTemplateId(1);
        inv.setCardTemplate(ct);
        inv.setQuantity(1);

        when(userStoryUnlockRepository.findByUser_CustomerIdAndIsActiveTrue(userId))
                .thenReturn(List.of(unlock));
        when(userInventoryRepository.findByUser_CustomerId(userId))
                .thenReturn(List.of(inv));

        // Act
        setStoryService.revokeStoriesIfConditionNotMet(userId);

        // Assert that delete is NEVER called
        verify(userStoryUnlockRepository, never()).delete(any());
        verify(userStoryUnlockRepository, never()).deleteById(any());

        // Assert that save is called with isActive = false
        ArgumentCaptor<UserStoryUnlock> captor = ArgumentCaptor.forClass(UserStoryUnlock.class);
        verify(userStoryUnlockRepository).save(captor.capture());

        UserStoryUnlock savedUnlock = captor.getValue();
        assertThat(savedUnlock.getIsActive()).isFalse();
    }

    // ── D04: Customer without unlock → StoryNotUnlockedException ──────────────

    @Test
    void getStoryById_customerNoUnlock_throwsStoryNotUnlockedException() {
        // Arrange
        Integer storyId = 10;
        Integer userId = 1;
        SetStory story = new SetStory();
        story.setStoryId(storyId);
        story.setTitle("Forbidden Story");

        when(setStoryRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(userStoryUnlockRepository
                .existsByUser_CustomerIdAndStory_StoryIdAndIsActiveTrue(userId, storyId))
                .thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> setStoryService.getStoryById(storyId, userId))
                .isInstanceOf(StoryNotUnlockedException.class)
                .hasMessageContaining("chưa mở khóa");
    }

    // ── D04: Customer with isActive=true unlock → full content returned ────────

    @Test
    void getStoryById_customerActiveUnlock_returnsStoryResponse() {
        // Arrange
        Integer storyId = 10;
        Integer userId = 1;
        SetStory story = new SetStory();
        story.setStoryId(storyId);
        story.setTitle("Unlocked Story");
        story.setContent("Secret tarot wisdom");

        when(setStoryRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(userStoryUnlockRepository
                .existsByUser_CustomerIdAndStory_StoryIdAndIsActiveTrue(userId, storyId))
                .thenReturn(true);

        // Act
        SetStoryResponse response = setStoryService.getStoryById(storyId, userId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStoryId()).isEqualTo(storyId);
        assertThat(response.getTitle()).isEqualTo("Unlocked Story");
        assertThat(response.getContent()).isEqualTo("Secret tarot wisdom");
    }

    // ── D04: Customer with isActive=false (revoked) → 403 ────────────────────

    @Test
    void getStoryById_customerRevokedUnlock_throwsStoryNotUnlockedException() {
        // Arrange — isActive=false means existsByIsActiveTrue returns false
        Integer storyId = 10;
        Integer userId = 1;
        SetStory story = new SetStory();
        story.setStoryId(storyId);

        when(setStoryRepository.findById(storyId)).thenReturn(Optional.of(story));
        when(userStoryUnlockRepository
                .existsByUser_CustomerIdAndStory_StoryIdAndIsActiveTrue(userId, storyId))
                .thenReturn(false); // revoked: isActive=false → existsByIsActiveTrue = false

        // Act + Assert
        assertThatThrownBy(() -> setStoryService.getStoryById(storyId, userId))
                .isInstanceOf(StoryNotUnlockedException.class);
    }

    // ── D04: Staff/Admin bypass — getStoryByIdNoGate returns content, no unlock check

    @Test
    void getStoryByIdNoGate_staffBypassesUnlockCheck_returnsStoryResponse() {
        // Arrange
        Integer storyId = 10;
        SetStory story = new SetStory();
        story.setStoryId(storyId);
        story.setTitle("Staff Viewable Story");
        story.setContent("Full content for staff");

        when(setStoryRepository.findById(storyId)).thenReturn(Optional.of(story));

        // Act
        SetStoryResponse response = setStoryService.getStoryByIdNoGate(storyId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Staff Viewable Story");

        // Unlock repository must NOT be consulted for the no-gate path
        verify(userStoryUnlockRepository, never())
                .existsByUser_CustomerIdAndStory_StoryIdAndIsActiveTrue(any(), any());
    }
}
