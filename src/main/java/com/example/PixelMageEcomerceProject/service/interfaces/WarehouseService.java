package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.WarehouseRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {

    /**
     * Create a new warehouse
     */
    Warehouse createWarehouse(WarehouseRequestDTO warehouseRequestDTO);

    /**
     * Update an existing warehouse
     */
    Warehouse updateWarehouse(Integer warehouseId, WarehouseRequestDTO warehouseRequestDTO);

    /**
     * Delete a warehouse
     */
    void deleteWarehouse(Integer warehouseId);

    /**
     * Get a warehouse by ID
     */
    Optional<Warehouse> getWarehouseById(Integer warehouseId);

    /**
     * Get all warehouses
     */
    List<Warehouse> getAllWarehouses();
}

