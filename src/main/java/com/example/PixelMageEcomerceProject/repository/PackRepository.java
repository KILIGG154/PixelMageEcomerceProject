package com.example.PixelMageEcomerceProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.Pack;
import com.example.PixelMageEcomerceProject.enums.PackStatus;

@Repository
public interface PackRepository extends JpaRepository<Pack, Integer> {
    List<Pack> findByStatus(PackStatus status);

    List<Pack> findByProductProductIdAndStatus(Integer productId, PackStatus status);
}
