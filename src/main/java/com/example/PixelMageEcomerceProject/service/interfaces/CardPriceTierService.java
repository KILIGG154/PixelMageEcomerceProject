package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.CardPriceTierRequestDTO;
import com.example.PixelMageEcomerceProject.entity.CardPriceTier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CardPriceTierService {
    CardPriceTier createCardPriceTier(CardPriceTierRequestDTO cardPriceTierRequestDTO);
    CardPriceTier updateCardPriceTier(Integer id, CardPriceTierRequestDTO cardPriceTierRequestDTO);
    void deleteCardPriceTier(Integer id);
    Optional<CardPriceTier> getCardPriceTierById(Integer id);
    List<CardPriceTier> getAllCardPriceTiers();
    List<CardPriceTier> getCardPriceTiersByTemplateId(Integer cardTemplateId);
}

