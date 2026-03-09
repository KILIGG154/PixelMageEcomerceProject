package com.example.PixelMageEcomerceProject.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.CardCollection;
import com.example.PixelMageEcomerceProject.entity.CollectionItem;
import com.example.PixelMageEcomerceProject.entity.UserCollectionProgress;
import com.example.PixelMageEcomerceProject.entity.UserInventory;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.CollectionItemRepository;
import com.example.PixelMageEcomerceProject.repository.UserCollectionProgressRepository;
import com.example.PixelMageEcomerceProject.repository.UserInventoryRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.UserCollectionProgressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCollectionProgressServiceImpl implements UserCollectionProgressService {

    private final UserCollectionProgressRepository progressRepository;
    private final CollectionItemRepository collectionItemRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final AccountRepository accountRepository;

    @Override
    public void recalculateProgressForTemplate(Integer userId, Integer cardTemplateId) {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + userId));

        // 1. Find all collections that require this template
        List<CollectionItem> itemsWithTemplate = collectionItemRepository
                .findByCardTemplateCardTemplateId(cardTemplateId);

        // 2. For each unique collection involved, recalculate progress
        itemsWithTemplate.stream()
                .map(item -> item.getCardCollection())
                .distinct()
                .forEach(collection -> recalculateCollectionProgress(user, collection));
    }

    private void recalculateCollectionProgress(Account user, CardCollection collection) {
        List<CollectionItem> allItems = collectionItemRepository
                .findByCardCollectionCollectionId(collection.getCollectionId());

        if (allItems.isEmpty()) {
            return;
        }

        int totalRequired = 0;
        int totalOwnedConfigured = 0;

        for (CollectionItem item : allItems) {
            totalRequired += item.getRequiredQuantity();

            Optional<UserInventory> invOpt = userInventoryRepository
                    .findByUser_CustomerIdAndCardTemplate_CardTemplateId(
                            user.getCustomerId(),
                            item.getCardTemplate().getCardTemplateId());

            int ownedQty = invOpt.isPresent() ? invOpt.get().getQuantity() : 0;
            // Cap the contribution of a single template to its required quantity
            totalOwnedConfigured += Math.min(ownedQty, item.getRequiredQuantity());
        }

        double percent = (totalRequired == 0) ? 0.0 : ((double) totalOwnedConfigured / totalRequired) * 100.0;
        boolean isCompleteNow = (percent >= 100.0);

        Optional<UserCollectionProgress> progressOpt = progressRepository
                .findByUserCustomerIdAndCollectionCollectionId(user.getCustomerId(), collection.getCollectionId());

        UserCollectionProgress progress;
        if (progressOpt.isPresent()) {
            progress = progressOpt.get();
        } else {
            progress = new UserCollectionProgress();
            progress.setUser(user);
            progress.setCollection(collection);
        }

        progress.setRequiredCount(totalRequired);
        progress.setOwnedCount(totalOwnedConfigured);
        progress.setCompletionPercent(percent);

        if (isCompleteNow && !Boolean.TRUE.equals(progress.getIsCompleted())) {
            progress.setIsCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());
            // Here you would trigger reward grant (idempotent because of isCompleted check
            // before this line)
            grantReward(user, collection);
        }

        progressRepository.save(progress);
    }

    private void grantReward(Account user, CardCollection collection) {
        // Implementation for granting reward to User
        // Check collection.getRewardType() / getRewardData()
        System.out.println("Granting reward to User " + user.getCustomerId() + " for completing "
                + collection.getCollectionName());
    }
}
