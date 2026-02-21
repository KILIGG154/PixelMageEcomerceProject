package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.CardContentRequestDTO;
import com.example.PixelMageEcomerceProject.entity.CardContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CardContentService {
    CardContent createCardContent(CardContentRequestDTO cardContentRequestDTO);
    CardContent updateCardContent(Integer id, CardContentRequestDTO cardContentRequestDTO);
    void deleteCardContent(Integer id);
    Optional<CardContent> getCardContentById(Integer id);
    List<CardContent> getAllCardContents();
    List<CardContent> getCardContentsByCardId(Integer cardId);
}

