package com.example.PixelMageEcomerceProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for client secret from Stripe operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientSecretResponseDTO {
    private String clientSecret;
    private String paymentIntentId;
    private String setupIntentId;
    private String publishableKey;
}