package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.CardContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardContentRepository extends JpaRepository<CardContent, Integer> {
    List<CardContent> findByCardCardId(Integer cardId);
}
