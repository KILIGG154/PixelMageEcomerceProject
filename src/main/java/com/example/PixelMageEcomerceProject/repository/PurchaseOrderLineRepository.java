package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, String> {
    PurchaseOrderLine findByPoId(String poId);
}
