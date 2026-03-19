package com.example.PixelMageEcomerceProject.exceptions;

/**
 * Thrown when an unlink-request verification token is past its 10-minute expiry.
 * GlobalExceptionHandler maps this to HTTP 410 Gone (TASK-05).
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
