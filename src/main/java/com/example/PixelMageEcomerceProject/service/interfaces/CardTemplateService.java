package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.CardTemplateRequestDTO;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CardTemplateService {
    CardTemplate createCardTemplate(CardTemplateRequestDTO cardTemplateRequestDTO);
    CardTemplate updateCardTemplate(Integer id, CardTemplateRequestDTO cardTemplateRequestDTO);
    void deleteCardTemplate(Integer id);
    Optional<CardTemplate> getCardTemplateById(Integer id);
    List<CardTemplate> getAllCardTemplates();
    Optional<CardTemplate> getCardTemplateByName(String name);
}

