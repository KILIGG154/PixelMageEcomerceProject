package com.example.PixelMageEcomerceProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.Spread;

@Repository
public interface SpreadRepository extends JpaRepository<Spread, Integer> {
    Optional<Spread> findByName(String name);
}
