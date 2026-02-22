package com.example.PixelMageEcomerceProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for saved payment method information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedPaymentMethodDTO {
    private String paymentMethodId;
    private String brand; // visa, mastercard, etc.
    private String last4; // Last 4 digits of card
    private Long expMonth;
    private Long expYear;
    private String fingerprint;
    private Boolean isDefault = false;
}