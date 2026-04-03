package com.example.PixelMageEcomerceProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for payment-related and general application
 * exceptions.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        // ── TASK-01 handlers ────────────────────────────────────────────────────

        @ExceptionHandler(ActiveSessionExistsException.class)
        public ResponseEntity<ResponseBase<Map<String, Object>>> handleActiveSessionExists(
                        ActiveSessionExistsException ex) {
                log.warn("Active session exists: sessionId={}", ex.getActiveSessionId());
                Map<String, Object> body = new java.util.HashMap<>();
                body.put("message", ex.getMessage());
                body.put("activeSessionId", ex.getActiveSessionId());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ResponseBase<>(HttpStatus.CONFLICT.value(), ex.getMessage(), body));
        }

        // ── TASK-06 handlers ───────────────────────────────────────────────────

        @ExceptionHandler(PackReservationException.class)
        public ResponseEntity<ResponseBase<Void>> handlePackReservation(PackReservationException ex) {
                log.warn("[LOCK] Pack reservation conflict: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.CONFLICT, ex.getMessage());
        }

        // ── End TASK-06 handlers ─────────────────────────────────────────────────

        @ExceptionHandler(RedisUnavailableException.class)
        public ResponseEntity<ResponseBase<Void>> handleRedisUnavailable(RedisUnavailableException ex) {
                log.error("Redis unavailable: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }

        @ExceptionHandler(SessionExpiredException.class)
        public ResponseEntity<ResponseBase<Void>> handleSessionExpired(SessionExpiredException ex) {
                log.warn("EXPLORE session expired: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.GONE, ex.getMessage());
        }

        // ── End TASK-01 handlers ─────────────────────────────────────────────────

        // ── TASK-D04 handlers ───────────────────────────────────────────────────

        @ExceptionHandler(StoryNotUnlockedException.class)
        public ResponseEntity<ResponseBase<Void>> handleStoryNotUnlocked(StoryNotUnlockedException ex) {
                log.warn("Story access denied: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.FORBIDDEN, ex.getMessage());
        }

        // ── End TASK-D04 handlers ─────────────────────────────────────────

        // ── TASK-05 handlers ─────────────────────────────────────────

        @ExceptionHandler(TokenExpiredException.class)
        public ResponseEntity<ResponseBase<Void>> handleTokenExpired(TokenExpiredException ex) {
                log.warn("Unlink verification token expired: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.GONE, ex.getMessage());
        }

        // ── End TASK-05 handlers ────────────────────────────────────────────────

        @ExceptionHandler(PaymentNotFoundException.class)
        public ResponseEntity<ResponseBase<Void>> handlePaymentNotFoundException(PaymentNotFoundException ex,
                        WebRequest request) {
                log.error("Payment not found: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.NOT_FOUND, ex.getMessage());
        }


        @ExceptionHandler(PaymentProcessingException.class)
        public ResponseEntity<ResponseBase<Void>> handlePaymentProcessingException(PaymentProcessingException ex,
                        WebRequest request) {
                log.error("Payment processing error: {}", ex.getMessage(), ex);
                return ResponseBase.error(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        @ExceptionHandler(InvalidPaymentStateException.class)
        public ResponseEntity<ResponseBase<Void>> handleInvalidPaymentStateException(InvalidPaymentStateException ex,
                        WebRequest request) {
                log.error("Invalid payment state: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.CONFLICT, ex.getMessage());
        }

        @ExceptionHandler(CardException.class)
        public ResponseEntity<ResponseBase<Void>> handleCardException(CardException ex, WebRequest request) {
                log.error("Stripe card error: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.BAD_REQUEST, "Card error: " + ex.getUserMessage());
        }

        @ExceptionHandler(RateLimitException.class)
        public ResponseEntity<ResponseBase<Void>> handleRateLimitException(RateLimitException ex, WebRequest request) {
                log.error("Stripe rate limit exceeded: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded. Please try again later.");
        }

        @ExceptionHandler(InvalidRequestException.class)
        public ResponseEntity<ResponseBase<Void>> handleInvalidRequestException(InvalidRequestException ex,
                        WebRequest request) {
                log.error("Stripe invalid request: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.BAD_REQUEST, "Invalid request: " + ex.getUserMessage());
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ResponseBase<Void>> handleAuthenticationException(AuthenticationException ex,
                        WebRequest request) {
                log.error("Stripe authentication error: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.UNAUTHORIZED, "Payment service authentication failed");
        }

        @ExceptionHandler(ApiConnectionException.class)
        public ResponseEntity<ResponseBase<Void>> handleApiConnectionException(ApiConnectionException ex,
                        WebRequest request) {
                log.error("Stripe API connection error: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.SERVICE_UNAVAILABLE,
                                "Payment service temporarily unavailable. Please try again later.");
        }

        @ExceptionHandler(ApiException.class)
        public ResponseEntity<ResponseBase<Void>> handleApiException(ApiException ex, WebRequest request) {
                log.error("Stripe API error: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Payment service error. Please try again later.");
        }

        @ExceptionHandler(StripeException.class)
        public ResponseEntity<ResponseBase<Void>> handleStripeException(StripeException ex, WebRequest request) {
                log.error("General Stripe error: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.BAD_REQUEST, "Payment processing failed: " + ex.getUserMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ResponseBase<Void>> handleIllegalArgumentException(IllegalArgumentException ex,
                        WebRequest request) {
                log.error("Invalid argument: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.BAD_REQUEST, "Invalid request parameters: " + ex.getMessage());
        }

        // ── Login Rate Limit handler ─────────────────────────────────────────────
        @ExceptionHandler(RateLimitExceededException.class)
        public ResponseEntity<ResponseBase<Void>> handleRateLimitExceeded(RateLimitExceededException ex) {
                log.warn("[RATE-LIMIT] Request blocked: {}", ex.getMessage());
                return ResponseBase.error(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ResponseBase<Void>> handleRuntimeException(RuntimeException ex, WebRequest request) {
                log.error("Runtime error: {}", ex.getMessage(), ex);
                return ResponseBase.error(HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred: " + ex.getMessage());
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ResponseBase<Void>> handleNoResourceFound(NoResourceFoundException ex) {
                // Trả về 404 thay vì 500 cho các yêu cầu resource không tồn tại (như favicon.ico)
                return ResponseBase.error(HttpStatus.NOT_FOUND, "Resource không tồn tại: " + ex.getResourcePath());
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBase<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new java.util.HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("[VALIDATION] Request failed with errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseBase<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseBase<Map<String, String>>> handleBindException(
            BindException ex) {
        Map<String, String> errors = new java.util.HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("[BINDING] Request failed with errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseBase<>(HttpStatus.BAD_REQUEST.value(), "Request binding failed", errors));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseBase<Void>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.warn("[MEDIA-TYPE] Unsupported media type: {}", ex.getMessage());
        return ResponseBase.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            "Định dạng yêu cầu không đúng (Content-Type). Server yêu cầu JSON hoặc Multipart.");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseBase<Void>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        log.warn("[UPLOAD] File size limit exceeded: {}", ex.getMessage());
        return ResponseBase.error(HttpStatus.PAYLOAD_TOO_LARGE, 
            "File quá lớn! Giới hạn tối đa là 10MB (cấu hình BE đã được update).");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBase<Void>> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseBase.error(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage());
    }
}
