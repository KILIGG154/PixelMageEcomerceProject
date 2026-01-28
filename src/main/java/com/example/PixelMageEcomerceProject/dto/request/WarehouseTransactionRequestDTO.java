package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseTransactionRequestDTO {
    private Integer warehouseId;
    private Integer productId;
    private Integer quantity;
    private String transactionType; // IN, OUT, ADJUSTMENT
    private Integer referenceId;
    private LocalDateTime transactionDate;
}

