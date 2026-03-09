package com.example.PixelMageEcomerceProject.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PixelMageEcomerceProject.dto.request.PackRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.entity.Pack;
import com.example.PixelMageEcomerceProject.entity.PackDetail;
import com.example.PixelMageEcomerceProject.entity.Product;
import com.example.PixelMageEcomerceProject.enums.CardProductStatus;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.CardRepository;
import com.example.PixelMageEcomerceProject.repository.CardTemplateRepository;
import com.example.PixelMageEcomerceProject.repository.PackDetailRepository;
import com.example.PixelMageEcomerceProject.repository.PackRepository;
import com.example.PixelMageEcomerceProject.repository.ProductRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.PackService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PackServiceImpl implements PackService {

    private final PackRepository packRepository;
    private final PackDetailRepository packDetailRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final CardTemplateRepository cardTemplateRepository;
    private final CardRepository cardRepository;

    private static final int CARDS_PER_PACK = 3;

    @Override
    public Pack createPack(PackRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + requestDTO.getProductId()));

        Account createdBy = null;
        if (requestDTO.getCreatedByAccountId() != null) {
            createdBy = accountRepository.findById(requestDTO.getCreatedByAccountId())
                    .orElseThrow(() -> new RuntimeException(
                            "Account not found with id: " + requestDTO.getCreatedByAccountId()));
        }

        // 1. Create Pack entity
        Pack pack = new Pack();
        pack.setProduct(product);
        pack.setStatus("CREATED");
        pack.setCreatedBy(createdBy);
        pack = packRepository.save(pack);

        // 2. Perform RNG to select Templates
        List<CardTemplate> allTemplates = cardTemplateRepository.findAll();
        if (allTemplates.isEmpty()) {
            throw new RuntimeException("No CardTemplates available to generate a pack");
        }

        Random random = new Random();
        List<PackDetail> packDetails = new ArrayList<>();

        for (int i = 0; i < CARDS_PER_PACK; i++) {
            // RNG logic: pick a random template
            CardTemplate selectedTemplate = allTemplates.get(random.nextInt(allTemplates.size()));

            // 3. Create physical Card instance (PENDING_BIND state)
            Card newCard = new Card();
            newCard.setCardTemplate(selectedTemplate);
            newCard.setProduct(product);
            newCard.setStatus(CardProductStatus.PENDING_BIND.name());
            newCard.setCardCondition("NEW");
            String batchStr = "BATCH-" + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue();
            newCard.setProductionBatch(batchStr);
            cardRepository.save(newCard);

            // 4. Create Pack Detail linking Pack -> Card
            PackDetail detail = new PackDetail();
            detail.setPack(pack);
            detail.setCard(newCard);
            detail.setPositionIndex(i + 1);
            packDetails.add(detail);
        }

        packDetailRepository.saveAll(packDetails);
        pack.setPackDetails(packDetails);

        return pack;
    }

    @Override
    public Pack updatePackStatus(Integer packId, String status) {
        Pack pack = packRepository.findById(packId)
                .orElseThrow(() -> new RuntimeException("Pack not found: " + packId));
        pack.setStatus(status);
        return packRepository.save(pack);
    }

    @Override
    public Optional<Pack> getPackById(Integer id) {
        return packRepository.findById(id);
    }

    @Override
    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }

    @Override
    public List<Pack> getPacksByStatus(String status) {
        return packRepository.findByStatus(status);
    }

    @Override
    public List<Pack> getPacksByProductAndStatus(Integer productId, String status) {
        return packRepository.findByProductProductIdAndStatus(productId, status);
    }

    @Override
    public void deletePack(Integer id) {
        packRepository.deleteById(id);
    }
}
