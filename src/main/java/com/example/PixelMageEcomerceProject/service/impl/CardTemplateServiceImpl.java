package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.CardTemplateRequestDTO;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.repository.CardTemplateRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.CardTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardTemplateServiceImpl implements CardTemplateService {

    private final CardTemplateRepository cardTemplateRepository;

    @Override
    public CardTemplate createCardTemplate(CardTemplateRequestDTO cardTemplateRequestDTO) {
        CardTemplate cardTemplate = new CardTemplate();
        cardTemplate.setName(cardTemplateRequestDTO.getName());
        cardTemplate.setDescription(cardTemplateRequestDTO.getDescription());
        cardTemplate.setDesignPath(cardTemplateRequestDTO.getDesignPath());
        return cardTemplateRepository.save(cardTemplate);
    }

    @Override
    public CardTemplate updateCardTemplate(Integer id, CardTemplateRequestDTO cardTemplateRequestDTO) {
        Optional<CardTemplate> existingTemplate = cardTemplateRepository.findById(id);
        if (existingTemplate.isPresent()) {
            CardTemplate updatedTemplate = existingTemplate.get();
            updatedTemplate.setName(cardTemplateRequestDTO.getName());
            updatedTemplate.setDescription(cardTemplateRequestDTO.getDescription());
            updatedTemplate.setDesignPath(cardTemplateRequestDTO.getDesignPath());
            return cardTemplateRepository.save(updatedTemplate);
        }
        throw new RuntimeException("CardTemplate not found with id: " + id);
    }

    @Override
    public void deleteCardTemplate(Integer id) {
        if (!cardTemplateRepository.existsById(id)) {
            throw new RuntimeException("CardTemplate not found with id: " + id);
        }
        cardTemplateRepository.deleteById(id);
    }

    @Override
    public Optional<CardTemplate> getCardTemplateById(Integer id) {
        return cardTemplateRepository.findById(id);
    }

    @Override
    public List<CardTemplate> getAllCardTemplates() {
        return cardTemplateRepository.findAll();
    }

    @Override
    public Optional<CardTemplate> getCardTemplateByName(String name) {
        return cardTemplateRepository.findByName(name);
    }
}
