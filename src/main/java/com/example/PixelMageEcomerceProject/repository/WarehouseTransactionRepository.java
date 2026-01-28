package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.WarehouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseTransactionRepository extends JpaRepository<WarehouseTransaction, Integer> {
    List<WarehouseTransaction> findByWarehouseId(Integer warehouseId);
    List<WarehouseTransaction> findByProductId(Integer productId);
}

