package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderRequestDTO;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrder;
import com.example.PixelMageEcomerceProject.entity.Supplier;
import com.example.PixelMageEcomerceProject.repository.PurchaseOrderRepository;
import com.example.PixelMageEcomerceProject.repository.SupplierRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        // Validate PO number uniqueness
        if (purchaseOrderRequestDTO.getPoNumber() != null &&
            existsByPoNumber(purchaseOrderRequestDTO.getPoNumber())) {
            throw new RuntimeException("PO number already exists: " + purchaseOrderRequestDTO.getPoNumber());
        }

        // Validate supplier exists
        if (purchaseOrderRequestDTO.getSupplierId() == null) {
            throw new RuntimeException("Supplier ID is required");
        }

        Supplier supplier = supplierRepository.findById(purchaseOrderRequestDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException(
                        "Supplier not found with id: " + purchaseOrderRequestDTO.getSupplierId() +
                        ". Please create the supplier first using POST /api/suppliers"
                ));

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseId(purchaseOrderRequestDTO.getWarehouseId());
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setPoNumber(purchaseOrderRequestDTO.getPoNumber());
        purchaseOrder.setStatus(purchaseOrderRequestDTO.getStatus());
        purchaseOrder.setOrderDate(purchaseOrderRequestDTO.getOrderDate());
        purchaseOrder.setExpectedDelivery(purchaseOrderRequestDTO.getExpectedDelivery());

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrder updatePurchaseOrder(Integer poId, PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        PurchaseOrder existingPO = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase order not found with id: " + poId));

        // Check if PO number is being changed and if new PO number already exists
        if (purchaseOrderRequestDTO.getPoNumber() != null &&
            !existingPO.getPoNumber().equals(purchaseOrderRequestDTO.getPoNumber()) &&
            existsByPoNumber(purchaseOrderRequestDTO.getPoNumber())) {
            throw new RuntimeException("PO number already exists: " + purchaseOrderRequestDTO.getPoNumber());
        }

        // Update supplier if provided
        if (purchaseOrderRequestDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(purchaseOrderRequestDTO.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + purchaseOrderRequestDTO.getSupplierId()));
            existingPO.setSupplier(supplier);
        }

        existingPO.setWarehouseId(purchaseOrderRequestDTO.getWarehouseId());
        existingPO.setPoNumber(purchaseOrderRequestDTO.getPoNumber());
        existingPO.setStatus(purchaseOrderRequestDTO.getStatus());
        existingPO.setOrderDate(purchaseOrderRequestDTO.getOrderDate());
        existingPO.setExpectedDelivery(purchaseOrderRequestDTO.getExpectedDelivery());

        return purchaseOrderRepository.save(existingPO);
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(Integer poId) {
        if (!purchaseOrderRepository.existsById(poId)) {
            throw new RuntimeException("Purchase order not found with id: " + poId);
        }
        purchaseOrderRepository.deleteById(poId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> getPurchaseOrderById(Integer poId) {
        return purchaseOrderRepository.findById(poId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> getPurchaseOrderByPoNumber(String poNumber) {
        return purchaseOrderRepository.findByPoNumber(poNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getPurchaseOrdersBySupplierId(Integer supplierId) {
        return purchaseOrderRepository.findBySupplierId(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPoNumber(String poNumber) {
        return purchaseOrderRepository.existsByPoNumber(poNumber);
    }
}

