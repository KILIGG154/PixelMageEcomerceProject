package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.CardContentRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.entity.CardContent;
import com.example.PixelMageEcomerceProject.repository.CardContentRepository;
import com.example.PixelMageEcomerceProject.repository.CardRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.CardContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardContentServiceImpl implements CardContentService {

    private final CardContentRepository cardContentRepository;
    private final CardRepository cardRepository;

    @Override
    public CardContent createCardContent(CardContentRequestDTO cardContentRequestDTO) {
        Card card = cardRepository.findById(cardContentRequestDTO.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardContentRequestDTO.getCardId()));

        CardContent cardContent = new CardContent();
        cardContent.setCard(card);
        cardContent.setContentType(cardContentRequestDTO.getContentType());
        cardContent.setContentData(cardContentRequestDTO.getContentData());
        cardContent.setPosition(cardContentRequestDTO.getPosition());
        return cardContentRepository.save(cardContent);
    }

    @Override
    public CardContent updateCardContent(Integer id, CardContentRequestDTO cardContentRequestDTO) {
        Optional<CardContent> existingContent = cardContentRepository.findById(id);
        if (existingContent.isPresent()) {
            CardContent updatedContent = existingContent.get();

            if (cardContentRequestDTO.getCardId() != null) {
                Card card = cardRepository.findById(cardContentRequestDTO.getCardId())
                        .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardContentRequestDTO.getCardId()));
                updatedContent.setCard(card);
            }

            updatedContent.setContentType(cardContentRequestDTO.getContentType());
            updatedContent.setContentData(cardContentRequestDTO.getContentData());
            updatedContent.setPosition(cardContentRequestDTO.getPosition());
            return cardContentRepository.save(updatedContent);
        }
        throw new RuntimeException("CardContent not found with id: " + id);
    }

    @Override
    public void deleteCardContent(Integer id) {
        if (!cardContentRepository.existsById(id)) {
            throw new RuntimeException("CardContent not found with id: " + id);
        }
        cardContentRepository.deleteById(id);
    }

    @Override
    public Optional<CardContent> getCardContentById(Integer id) {
        return cardContentRepository.findById(id);
    }

    @Override
    public List<CardContent> getAllCardContents() {
        return cardContentRepository.findAll();
    }

    @Override
    public List<CardContent> getCardContentsByCardId(Integer cardId) {
        return cardContentRepository.findByCardCardId(cardId);
    }
}
