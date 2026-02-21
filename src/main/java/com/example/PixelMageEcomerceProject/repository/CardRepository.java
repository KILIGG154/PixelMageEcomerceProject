package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Optional<Card> findByNfcUuid(String nfcUuid);
}
