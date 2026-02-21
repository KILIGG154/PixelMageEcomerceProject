package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.CardPriceTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardPriceTierRepository extends JpaRepository<CardPriceTier, Integer> {
    List<CardPriceTier> findByCardTemplateCardTemplateId(Integer cardTemplateId);
}
