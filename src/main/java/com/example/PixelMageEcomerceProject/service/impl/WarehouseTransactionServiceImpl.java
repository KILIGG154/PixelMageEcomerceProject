package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.WarehouseTransactionRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Inventory;
import com.example.PixelMageEcomerceProject.entity.Warehouse;
import com.example.PixelMageEcomerceProject.entity.WarehouseTransaction;
import com.example.PixelMageEcomerceProject.repository.InventoryRepository;
import com.example.PixelMageEcomerceProject.repository.WarehouseRepository;
import com.example.PixelMageEcomerceProject.repository.WarehouseTransactionRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.WarehouseTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseTransactionServiceImpl implements WarehouseTransactionService {

    private final WarehouseTransactionRepository transactionRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public WarehouseTransaction createTransaction(WarehouseTransactionRequestDTO transactionRequestDTO) {
        Warehouse warehouse = warehouseRepository.findById(transactionRequestDTO.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + transactionRequestDTO.getWarehouseId()));

        WarehouseTransaction transaction = new WarehouseTransaction();
        transaction.setWarehouse(warehouse);
        transaction.setProductId(transactionRequestDTO.getProductId());
        transaction.setQuantity(transactionRequestDTO.getQuantity());
        transaction.setTransactionType(transactionRequestDTO.getTransactionType());
        transaction.setReferenceId(transactionRequestDTO.getReferenceId());
        transaction.setTransactionDate(transactionRequestDTO.getTransactionDate());

        // Save transaction
        WarehouseTransaction savedTransaction = transactionRepository.save(transaction);

        // Update inventory based on transaction type
        updateInventoryFromTransaction(transactionRequestDTO);

        return savedTransaction;
    }

    @Override
    @Transactional
    public WarehouseTransaction updateTransaction(Integer transactionId, WarehouseTransactionRequestDTO transactionRequestDTO) {
        WarehouseTransaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));

        Warehouse warehouse = warehouseRepository.findById(transactionRequestDTO.getWarehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + transactionRequestDTO.getWarehouseId()));

        existingTransaction.setWarehouse(warehouse);
        existingTransaction.setProductId(transactionRequestDTO.getProductId());
        existingTransaction.setQuantity(transactionRequestDTO.getQuantity());
        existingTransaction.setTransactionType(transactionRequestDTO.getTransactionType());
        existingTransaction.setReferenceId(transactionRequestDTO.getReferenceId());
        existingTransaction.setTransactionDate(transactionRequestDTO.getTransactionDate());

        return transactionRepository.save(existingTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Integer transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new RuntimeException("Transaction not found with id: " + transactionId);
        }
        transactionRepository.deleteById(transactionId);
    }

    @Override
    public Optional<WarehouseTransaction> getTransactionById(Integer transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public List<WarehouseTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<WarehouseTransaction> getTransactionsByWarehouseId(Integer warehouseId) {
        return transactionRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public List<WarehouseTransaction> getTransactionsByProductId(Integer productId) {
        return transactionRepository.findByProductId(productId);
    }

    /**
     * Update inventory based on warehouse transaction
     * This demonstrates: WAREHOUSE_TRANSACTION ──updates──> INVENTORY
     */
    private void updateInventoryFromTransaction(WarehouseTransactionRequestDTO transactionRequestDTO) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByWarehouseIdAndProductId(
                transactionRequestDTO.getWarehouseId(),
                transactionRequestDTO.getProductId()
        );

        Inventory inventory;
        if (inventoryOpt.isPresent()) {
            inventory = inventoryOpt.get();
        } else {
            // Create new inventory if it doesn't exist
            Warehouse warehouse = warehouseRepository.findById(transactionRequestDTO.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found"));
            inventory = new Inventory();
            inventory.setWarehouse(warehouse);
            inventory.setProductId(transactionRequestDTO.getProductId());
            inventory.setQuantity(0);
        }

        // Update quantity based on transaction type
        int currentQuantity = inventory.getQuantity();
        int transactionQuantity = transactionRequestDTO.getQuantity();

        switch (transactionRequestDTO.getTransactionType().toUpperCase()) {
            case "IN":
                inventory.setQuantity(currentQuantity + transactionQuantity);
                break;
            case "OUT":
                if (currentQuantity < transactionQuantity) {
                    throw new RuntimeException("Insufficient inventory for product " + transactionRequestDTO.getProductId());
                }
                inventory.setQuantity(currentQuantity - transactionQuantity);
                break;
            case "ADJUSTMENT":
                inventory.setQuantity(transactionQuantity);
                break;
            default:
                throw new RuntimeException("Invalid transaction type: " + transactionRequestDTO.getTransactionType());
        }

        inventoryRepository.save(inventory);
    }
}

