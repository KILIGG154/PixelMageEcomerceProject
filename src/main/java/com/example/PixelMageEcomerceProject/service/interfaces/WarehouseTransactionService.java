package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.WarehouseTransactionRequestDTO;
import com.example.PixelMageEcomerceProject.entity.WarehouseTransaction;

import java.util.List;
import java.util.Optional;

public interface WarehouseTransactionService {

    /**
     * Create a new warehouse transaction
     */
    WarehouseTransaction createTransaction(WarehouseTransactionRequestDTO transactionRequestDTO);

    /**
     * Update an existing warehouse transaction
     */
    WarehouseTransaction updateTransaction(Integer transactionId, WarehouseTransactionRequestDTO transactionRequestDTO);

    /**
     * Delete a warehouse transaction
     */
    void deleteTransaction(Integer transactionId);

    /**
     * Get a warehouse transaction by ID
     */
    Optional<WarehouseTransaction> getTransactionById(Integer transactionId);

    /**
     * Get all warehouse transactions
     */
    List<WarehouseTransaction> getAllTransactions();

    /**
     * Get transactions by warehouse ID
     */
    List<WarehouseTransaction> getTransactionsByWarehouseId(Integer warehouseId);

    /**
     * Get transactions by product ID
     */
    List<WarehouseTransaction> getTransactionsByProductId(Integer productId);
}

