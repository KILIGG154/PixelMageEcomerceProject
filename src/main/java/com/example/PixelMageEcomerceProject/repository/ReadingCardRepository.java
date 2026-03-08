package com.example.PixelMageEcomerceProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.ReadingCard;

@Repository
public interface ReadingCardRepository extends JpaRepository<ReadingCard, Integer> {
    List<ReadingCard> findByReadingSession_ReadingSessionId(Integer readingSessionId);
}
