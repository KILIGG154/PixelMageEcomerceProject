package com.example.PixelMageEcomerceProject.exceptions;

/**
 * Exception thrown when a payment is not found.
 */
public class PaymentNotFoundException extends RuntimeException {
    
    public PaymentNotFoundException(String message) {
        super(message);
    }
    
    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static PaymentNotFoundException forOrderId(Integer orderId) {
        return new PaymentNotFoundException("Payment not found for order ID: " + orderId);
    }
    
    public static PaymentNotFoundException forPaymentIntentId(String paymentIntentId) {
        return new PaymentNotFoundException("Payment not found for payment intent ID: " + paymentIntentId);
    }
    
    public static PaymentNotFoundException forPaymentId(Integer paymentId) {
        return new PaymentNotFoundException("Payment not found with ID: " + paymentId);
    }
}