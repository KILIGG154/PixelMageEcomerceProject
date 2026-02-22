package com.example.PixelMageEcomerceProject.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class for Stripe payment integration.
 * Initializes Stripe with the secret API key from application properties.
 */
@Configuration
public class PaymentConfig {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.api.public-key}")
    private String stripePublicKey;

    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String getStripePublicKey() {
        return stripePublicKey;
    }
}