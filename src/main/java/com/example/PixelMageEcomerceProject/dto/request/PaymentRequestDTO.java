package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a payment intent.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Integer orderId;
    private BigDecimal amount;
    private String currency; // USD, VND, etc.
    private String paymentMethod; // CARD, BANK_TRANSFER, etc.
    private Boolean savePaymentMethod = false;
}