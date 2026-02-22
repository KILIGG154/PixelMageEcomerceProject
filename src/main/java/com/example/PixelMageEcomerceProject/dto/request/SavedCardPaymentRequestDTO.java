package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for payment with saved card.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedCardPaymentRequestDTO {
    private Integer orderId;
    private String paymentMethodId; // Stripe payment method ID
}