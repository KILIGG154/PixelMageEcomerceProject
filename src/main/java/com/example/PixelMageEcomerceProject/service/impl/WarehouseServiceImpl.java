package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.WarehouseRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Warehouse;
import com.example.PixelMageEcomerceProject.repository.WarehouseRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public Warehouse createWarehouse(WarehouseRequestDTO warehouseRequestDTO) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseRequestDTO.getName());
        warehouse.setAddress(warehouseRequestDTO.getAddress());
        warehouse.setCity(warehouseRequestDTO.getCity());
        warehouse.setState(warehouseRequestDTO.getState());
        warehouse.setZipCode(warehouseRequestDTO.getZipCode());

        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public Warehouse updateWarehouse(Integer warehouseId, WarehouseRequestDTO warehouseRequestDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));

        existingWarehouse.setName(warehouseRequestDTO.getName());
        existingWarehouse.setAddress(warehouseRequestDTO.getAddress());
        existingWarehouse.setCity(warehouseRequestDTO.getCity());
        existingWarehouse.setState(warehouseRequestDTO.getState());
        existingWarehouse.setZipCode(warehouseRequestDTO.getZipCode());

        return warehouseRepository.save(existingWarehouse);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Integer warehouseId) {
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new RuntimeException("Warehouse not found with id: " + warehouseId);
        }
        warehouseRepository.deleteById(warehouseId);
    }

    @Override
    public Optional<Warehouse> getWarehouseById(Integer warehouseId) {
        return warehouseRepository.findById(warehouseId);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
}

