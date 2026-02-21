package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByWarehouseId(Integer warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.warehouse.id = :warehouseId AND i.product.productId = :productId")
    Optional<Inventory> findByWarehouseIdAndProductId(@Param("warehouseId") Integer warehouseId, @Param("productId") Integer productId);
}

