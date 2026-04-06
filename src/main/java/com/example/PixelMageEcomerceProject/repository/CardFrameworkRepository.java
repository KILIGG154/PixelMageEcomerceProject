package com.example.PixelMageEcomerceProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.CardFramework;

import java.util.List;

@Repository
public interface CardFrameworkRepository extends JpaRepository<CardFramework, Integer> {
    List<CardFramework> findByActiveTrueOrderByNameAsc();
}
