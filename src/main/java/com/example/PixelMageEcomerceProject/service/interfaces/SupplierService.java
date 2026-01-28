package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.SupplierRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {

    /**
     * Create a new supplier
     */
    Supplier createSupplier(SupplierRequestDTO supplierRequestDTO);

    /**
     * Update an existing supplier
     */
    Supplier updateSupplier(Integer supplierId, SupplierRequestDTO supplierRequestDTO);

    /**
     * Delete a supplier by ID
     */
    void deleteSupplier(Integer supplierId);

    /**
     * Get supplier by ID
     */
    Optional<Supplier> getSupplierById(Integer supplierId);

    /**
     * Get supplier by email
     */
    Optional<Supplier> getSupplierByEmail(String email);

    /**
     * Get supplier by name
     */
    Optional<Supplier> getSupplierByName(String name);

    /**
     * Get all suppliers
     */
    List<Supplier> getAllSuppliers();

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}

