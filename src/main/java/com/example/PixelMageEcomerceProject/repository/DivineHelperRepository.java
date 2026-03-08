package com.example.PixelMageEcomerceProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.DivineHelper;

@Repository
public interface DivineHelperRepository extends JpaRepository<DivineHelper, Integer> {
    Optional<DivineHelper> findByCardTemplate_CardTemplateId(Integer cardTemplateId);
}
