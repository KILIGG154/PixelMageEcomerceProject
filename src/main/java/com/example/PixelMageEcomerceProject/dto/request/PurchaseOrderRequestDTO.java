package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequestDTO {

    private Integer warehouseId;
    private Integer supplierId;
    private String poNumber;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDelivery;
}

