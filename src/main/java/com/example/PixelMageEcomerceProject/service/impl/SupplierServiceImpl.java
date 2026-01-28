package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.SupplierRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Supplier;
import com.example.PixelMageEcomerceProject.repository.SupplierRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public Supplier createSupplier(SupplierRequestDTO supplierRequestDTO) {
        // Validate email
        if (existsByEmail(supplierRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + supplierRequestDTO.getEmail());
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierRequestDTO.getName());
        supplier.setContactPerson(supplierRequestDTO.getContactPerson());
        supplier.setEmail(supplierRequestDTO.getEmail());
        supplier.setPhone(supplierRequestDTO.getPhone());
        supplier.setAddress(supplierRequestDTO.getAddress());

        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Integer supplierId, SupplierRequestDTO supplierRequestDTO) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));

        // Check if email is being changed and if new email already exists
        if (!existingSupplier.getEmail().equals(supplierRequestDTO.getEmail()) &&
            existsByEmail(supplierRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + supplierRequestDTO.getEmail());
        }

        existingSupplier.setName(supplierRequestDTO.getName());
        existingSupplier.setContactPerson(supplierRequestDTO.getContactPerson());
        existingSupplier.setEmail(supplierRequestDTO.getEmail());
        existingSupplier.setPhone(supplierRequestDTO.getPhone());
        existingSupplier.setAddress(supplierRequestDTO.getAddress());

        return supplierRepository.save(existingSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Integer supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            throw new RuntimeException("Supplier not found with id: " + supplierId);
        }
        supplierRepository.deleteById(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Integer supplierId) {
        return supplierRepository.findById(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByName(String name) {
        return supplierRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return supplierRepository.existsByEmail(email);
    }
}

