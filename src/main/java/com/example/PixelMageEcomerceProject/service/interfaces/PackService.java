package com.example.PixelMageEcomerceProject.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.PixelMageEcomerceProject.dto.request.PackRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Pack;
import com.example.PixelMageEcomerceProject.enums.PackStatus;

public interface PackService {
    Pack createPack(PackRequestDTO requestDTO); // This should run RNG to select cards

    Optional<Pack> getPackById(Integer id);

    List<Pack> getAllPacks();

    List<Pack> getPacksByStatus(PackStatus status);

    List<Pack> getPacksByProductAndStatus(Integer productId, PackStatus status);

    void deletePack(Integer id);

    Pack updatePackStatus(Integer packId, PackStatus status);
}
