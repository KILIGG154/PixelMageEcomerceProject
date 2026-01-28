package com.example.PixelMageEcomerceProject.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderLineRequest {
    private int quantityOrdered;
    private int quantityReceived;
    private int quantityPendingReceived;
    private double unitPrice;
    private double totalPrice;
    private LocalDate expectedDate;
    private String note;
}
