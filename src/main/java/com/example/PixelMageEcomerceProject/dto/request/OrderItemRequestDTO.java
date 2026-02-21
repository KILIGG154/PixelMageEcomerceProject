package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
    private Integer orderId;
    private Integer cardId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String customText;
}

