package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    List<PurchaseOrder> findByStatus(String status);

    List<PurchaseOrder> findBySupplierId(Integer supplierId);

    boolean existsByPoNumber(String poNumber);
}

