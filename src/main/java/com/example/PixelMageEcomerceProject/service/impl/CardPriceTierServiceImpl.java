package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.CardPriceTierRequestDTO;
import com.example.PixelMageEcomerceProject.entity.CardPriceTier;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.repository.CardPriceTierRepository;
import com.example.PixelMageEcomerceProject.repository.CardTemplateRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.CardPriceTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardPriceTierServiceImpl implements CardPriceTierService {

    private final CardPriceTierRepository cardPriceTierRepository;
    private final CardTemplateRepository cardTemplateRepository;

    @Override
    public CardPriceTier createCardPriceTier(CardPriceTierRequestDTO cardPriceTierRequestDTO) {
        CardTemplate cardTemplate = cardTemplateRepository.findById(cardPriceTierRequestDTO.getCardTemplateId())
                .orElseThrow(() -> new RuntimeException("CardTemplate not found with id: " + cardPriceTierRequestDTO.getCardTemplateId()));

        CardPriceTier cardPriceTier = new CardPriceTier();
        cardPriceTier.setCardTemplate(cardTemplate);
        cardPriceTier.setMinQuantity(cardPriceTierRequestDTO.getMinQuantity());
        cardPriceTier.setMaxQuantity(cardPriceTierRequestDTO.getMaxQuantity());
        cardPriceTier.setPricePerUnit(cardPriceTierRequestDTO.getPricePerUnit());
        return cardPriceTierRepository.save(cardPriceTier);
    }

    @Override
    public CardPriceTier updateCardPriceTier(Integer id, CardPriceTierRequestDTO cardPriceTierRequestDTO) {
        Optional<CardPriceTier> existingTier = cardPriceTierRepository.findById(id);
        if (existingTier.isPresent()) {
            CardPriceTier updatedTier = existingTier.get();

            if (cardPriceTierRequestDTO.getCardTemplateId() != null) {
                CardTemplate cardTemplate = cardTemplateRepository.findById(cardPriceTierRequestDTO.getCardTemplateId())
                        .orElseThrow(() -> new RuntimeException("CardTemplate not found with id: " + cardPriceTierRequestDTO.getCardTemplateId()));
                updatedTier.setCardTemplate(cardTemplate);
            }

            updatedTier.setMinQuantity(cardPriceTierRequestDTO.getMinQuantity());
            updatedTier.setMaxQuantity(cardPriceTierRequestDTO.getMaxQuantity());
            updatedTier.setPricePerUnit(cardPriceTierRequestDTO.getPricePerUnit());
            return cardPriceTierRepository.save(updatedTier);
        }
        throw new RuntimeException("CardPriceTier not found with id: " + id);
    }

    @Override
    public void deleteCardPriceTier(Integer id) {
        if (!cardPriceTierRepository.existsById(id)) {
            throw new RuntimeException("CardPriceTier not found with id: " + id);
        }
        cardPriceTierRepository.deleteById(id);
    }

    @Override
    public Optional<CardPriceTier> getCardPriceTierById(Integer id) {
        return cardPriceTierRepository.findById(id);
    }

    @Override
    public List<CardPriceTier> getAllCardPriceTiers() {
        return cardPriceTierRepository.findAll();
    }

    @Override
    public List<CardPriceTier> getCardPriceTiersByTemplateId(Integer cardTemplateId) {
        return cardPriceTierRepository.findByCardTemplateCardTemplateId(cardTemplateId);
    }
}
