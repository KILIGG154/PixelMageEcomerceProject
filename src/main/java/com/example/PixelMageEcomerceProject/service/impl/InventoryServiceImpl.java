package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.InventoryRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Inventory;
import com.example.PixelMageEcomerceProject.entity.Product;
import com.example.PixelMageEcomerceProject.entity.Warehouse;
import com.example.PixelMageEcomerceProject.repository.InventoryRepository;
import com.example.PixelMageEcomerceProject.repository.ProductRepository;
import com.example.PixelMageEcomerceProject.repository.WarehouseRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Inventory createInventory(InventoryRequestDTO inventoryRequestDTO, int productId) {
        Warehouse warehouse = warehouseRepository.findById(inventoryRequestDTO.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + inventoryRequestDTO.getWarehouseId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + inventoryRequestDTO.getProductId()));

        // Check if inventory already exists for this product in this warehouse
        Optional<Inventory> existingInventory = inventoryRepository.findByWarehouseIdAndProductId(
                inventoryRequestDTO.getWarehouseId(),
                inventoryRequestDTO.getProductId()
        );

        if (existingInventory.isPresent()) {
            throw new RuntimeException("Inventory already exists for product " + inventoryRequestDTO.getProductId() +
                    " in warehouse " + inventoryRequestDTO.getWarehouseId());
        }

        Inventory inventory = new Inventory();
        inventory.setWarehouse(warehouse);
        inventory.setProduct(product);
        inventory.setQuantity(inventoryRequestDTO.getQuantity());
        inventory.setLastChecked(inventoryRequestDTO.getLastChecked());

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory updateInventory(Integer inventoryId, InventoryRequestDTO inventoryRequestDTO) {
        Inventory existingInventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));

        Warehouse warehouse = warehouseRepository.findById(inventoryRequestDTO.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + inventoryRequestDTO.getWarehouseId()));

        existingInventory.setWarehouse(warehouse);
//        existingInventory.setProductId(inventoryRequestDTO.getProductId());
        existingInventory.setQuantity(inventoryRequestDTO.getQuantity());
        existingInventory.setLastChecked(inventoryRequestDTO.getLastChecked());

        return inventoryRepository.save(existingInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(Integer inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new RuntimeException("Inventory not found with id: " + inventoryId);
        }
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    public Optional<Inventory> getInventoryById(Integer inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> getInventoryByWarehouseId(Integer warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }
}

