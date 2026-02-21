package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.CardRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.entity.Product;
import com.example.PixelMageEcomerceProject.repository.CardRepository;
import com.example.PixelMageEcomerceProject.repository.CardTemplateRepository;
import com.example.PixelMageEcomerceProject.repository.ProductRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardTemplateRepository cardTemplateRepository;
    private final ProductRepository productRepository;

    @Override
    public Card createCard(CardRequestDTO cardRequestDTO) {
        CardTemplate cardTemplate = cardTemplateRepository.findById(cardRequestDTO.getCardTemplateId())
                .orElseThrow(() -> new RuntimeException("CardTemplate not found with id: " + cardRequestDTO.getCardTemplateId()));

        Product product = productRepository.findById(cardRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cardRequestDTO.getProductId()));

        Card card = new Card();
        card.setNfcUuid(cardRequestDTO.getNfcUuid());
        card.setCardTemplate(cardTemplate);
        card.setProduct(product);
        card.setCustomText(cardRequestDTO.getCustomText());

        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Integer id, CardRequestDTO cardRequestDTO) {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isPresent()) {
            Card updatedCard = existingCard.get();
            updatedCard.setNfcUuid(cardRequestDTO.getNfcUuid());

            if (cardRequestDTO.getCardTemplateId() != null) {
                CardTemplate cardTemplate = cardTemplateRepository.findById(cardRequestDTO.getCardTemplateId())
                        .orElseThrow(() -> new RuntimeException("CardTemplate not found with id: " + cardRequestDTO.getCardTemplateId()));
                updatedCard.setCardTemplate(cardTemplate);
            }

            if (cardRequestDTO.getProductId() != null) {
                Product product = productRepository.findById(cardRequestDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + cardRequestDTO.getProductId()));
                updatedCard.setProduct(product);
            }

            updatedCard.setCustomText(cardRequestDTO.getCustomText());
            return cardRepository.save(updatedCard);
        }
        throw new RuntimeException("Card not found with id: " + id);
    }

    @Override
    public void deleteCard(Integer id) {
        if (!cardRepository.existsById(id)) {
            throw new RuntimeException("Card not found with id: " + id);
        }
        cardRepository.deleteById(id);
    }

    @Override
    public Optional<Card> getCardById(Integer id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Optional<Card> getCardByNfcUuid(String nfcUuid) {
        return cardRepository.findByNfcUuid(nfcUuid);
    }
}
