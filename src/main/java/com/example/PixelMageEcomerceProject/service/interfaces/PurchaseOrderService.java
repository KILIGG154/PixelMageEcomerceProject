package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderRequestDTO;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrder;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderService {

    /**
     * Create a new purchase order
     */
    PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO);

    /**
     * Update an existing purchase order
     */
    PurchaseOrder updatePurchaseOrder(Integer poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO);

    /**
     * Delete a purchase order by ID
     */
    void deletePurchaseOrder(Integer poId);

    /**
     * Get purchase order by ID
     */
    Optional<PurchaseOrder> getPurchaseOrderById(Integer poId);

    /**
     * Get purchase order by PO number
     */
    Optional<PurchaseOrder> getPurchaseOrderByPoNumber(String poNumber);

    /**
     * Get purchase orders by status
     */
    List<PurchaseOrder> getPurchaseOrdersByStatus(String status);

    /**
     * Get purchase orders by supplier ID
     */
    List<PurchaseOrder> getPurchaseOrdersBySupplierId(Integer supplierId);

    /**
     * Get all purchase orders
     */
    List<PurchaseOrder> getAllPurchaseOrders();

    /**
     * Check if PO number exists
     */
    boolean existsByPoNumber(String poNumber);
}

