package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.CollectionItemRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.CollectionRequestDTO;
import com.example.PixelMageEcomerceProject.entity.*;
import com.example.PixelMageEcomerceProject.repository.*;
import com.example.PixelMageEcomerceProject.service.interfaces.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final CardCollectionRepository cardCollectionRepository;
    private final CollectionItemRepository collectionItemRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final OrderRepository orderRepository;

    // ==================== Collection CRUD ====================

    @Override
    public CardCollection createCollection(Integer customerId, CollectionRequestDTO request) {
        Account account = accountRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + customerId));

        CardCollection collection = new CardCollection();
        collection.setAccount(account);
        collection.setCollectionName(request.getCollectionName());
        collection.setDescription(request.getDescription());
        collection.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);

        return cardCollectionRepository.save(collection);
    }

    @Override
    public CardCollection updateCollection(Integer customerId, Integer collectionId, CollectionRequestDTO request) {
        CardCollection collection = cardCollectionRepository
                .findByCollectionIdAndAccountCustomerId(collectionId, customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Collection not found with id: " + collectionId + " for customer: " + customerId));

        if (request.getCollectionName() != null) {
            collection.setCollectionName(request.getCollectionName());
        }
        if (request.getDescription() != null) {
            collection.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            collection.setIsPublic(request.getIsPublic());
        }

        return cardCollectionRepository.save(collection);
    }

    @Override
    public void deleteCollection(Integer customerId, Integer collectionId) {
        CardCollection collection = cardCollectionRepository
                .findByCollectionIdAndAccountCustomerId(collectionId, customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Collection not found with id: " + collectionId + " for customer: " + customerId));

        cardCollectionRepository.delete(collection);
    }

    @Override
    public Optional<CardCollection> getCollectionById(Integer collectionId) {
        return cardCollectionRepository.findById(collectionId);
    }

    @Override
    public List<CardCollection> getCollectionsByCustomerId(Integer customerId) {
        return cardCollectionRepository.findByAccountCustomerId(customerId);
    }

    @Override
    public List<CardCollection> getPublicCollections() {
        return cardCollectionRepository.findAllPublicCollections();
    }

    // ==================== Collection Items ====================

    @Override
    public CollectionItem addCardToCollection(Integer customerId, CollectionItemRequestDTO request) {
        // 1. Verify collection belongs to customer
        CardCollection collection = cardCollectionRepository
                .findByCollectionIdAndAccountCustomerId(request.getCollectionId(), customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Collection not found with id: " + request.getCollectionId() + " for customer: " + customerId));

        // 2. Verify card exists
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + request.getCardId()));

        // 3. Verify customer owns the card (purchased through completed & paid order)
        if (!isCardOwnedByCustomer(customerId, request.getCardId())) {
            throw new RuntimeException(
                    "Card with id: " + request.getCardId() + " is not owned by customer: " + customerId
                            + ". Card must be purchased (order COMPLETED & PAID) before adding to collection.");
        }

        // 4. Check if card is already in collection
        if (collectionItemRepository.existsByCardCollectionCollectionIdAndCardCardId(
                request.getCollectionId(), request.getCardId())) {
            throw new RuntimeException("Card is already in this collection.");
        }

        // 5. Add card to collection
        CollectionItem item = new CollectionItem();
        item.setCardCollection(collection);
        item.setCard(card);

        return collectionItemRepository.save(item);
    }

    @Override
    public void removeCardFromCollection(Integer customerId, Integer collectionId, Integer cardId) {
        // Verify collection belongs to customer
        cardCollectionRepository.findByCollectionIdAndAccountCustomerId(collectionId, customerId)
                .orElseThrow(() -> new RuntimeException(
                        "Collection not found with id: " + collectionId + " for customer: " + customerId));

        // Verify item exists
        CollectionItem item = collectionItemRepository
                .findByCardCollectionCollectionIdAndCardCardId(collectionId, cardId)
                .orElseThrow(() -> new RuntimeException(
                        "Card with id: " + cardId + " not found in collection: " + collectionId));

        collectionItemRepository.delete(item);
    }

    @Override
    public List<CollectionItem> getCollectionItems(Integer collectionId) {
        return collectionItemRepository.findByCardCollectionCollectionId(collectionId);
    }

    // ==================== Owned Cards ====================

    /**
     * Get all cards owned by a customer.
     * A card is "owned" when it was purchased through a COMPLETED order with PAID payment status.
     *
     * Chain: Account -> Order (COMPLETED + PAID) -> OrderItem -> Card
     */
    @Override
    public List<Card> getOwnedCards(Integer customerId) {
        List<Order> completedOrders = orderRepository.findByAccountCustomerId(customerId)
                .stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()) && "PAID".equals(order.getPaymentStatus()))
                .collect(Collectors.toList());

        return completedOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getCard)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Check if a specific card is owned by a customer.
     */
    @Override
    public boolean isCardOwnedByCustomer(Integer customerId, Integer cardId) {
        return orderRepository.findByAccountCustomerId(customerId)
                .stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()) && "PAID".equals(order.getPaymentStatus()))
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getCard().getCardId().equals(cardId));
    }
}
