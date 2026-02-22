package com.example.PixelMageEcomerceProject.exceptions;

/**
 * Exception thrown when a payment operation is attempted on an invalid payment state.
 */
public class InvalidPaymentStateException extends RuntimeException {
    
    private final String currentState;
    private final String expectedState;
    
    public InvalidPaymentStateException(String message) {
        super(message);
        this.currentState = null;
        this.expectedState = null;
    }
    
    public InvalidPaymentStateException(String message, String currentState, String expectedState) {
        super(message);
        this.currentState = currentState;
        this.expectedState = expectedState;
    }
    
    public InvalidPaymentStateException(String message, Throwable cause) {
        super(message, cause);
        this.currentState = null;
        this.expectedState = null;
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public String getExpectedState() {
        return expectedState;
    }
    
    public static InvalidPaymentStateException forState(String currentState, String expectedState) {
        return new InvalidPaymentStateException(
                String.format("Invalid payment state. Expected: %s, but was: %s", expectedState, currentState),
                currentState, 
                expectedState
        );
    }
}