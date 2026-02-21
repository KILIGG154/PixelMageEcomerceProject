package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardTemplateRepository extends JpaRepository<CardTemplate, Integer> {
    Optional<CardTemplate> findByName(String name);
}
