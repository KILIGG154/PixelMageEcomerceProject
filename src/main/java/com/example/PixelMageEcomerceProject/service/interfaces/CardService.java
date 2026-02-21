package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.CardRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Card;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CardService {
    Card createCard(CardRequestDTO cardRequestDTO);
    Card updateCard(Integer id, CardRequestDTO cardRequestDTO);
    void deleteCard(Integer id);
    Optional<Card> getCardById(Integer id);
    List<Card> getAllCards();
    Optional<Card> getCardByNfcUuid(String nfcUuid);
}

