package com.example.PixelMageEcomerceProject.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {

    /**
     * Creates a VNPAY payment URL for the given order ID and amount.
     */
    String createPaymentUrl(Integer orderId, int amount, String orderInfo, String ipAddress);

    /**
     * Processes the return callback from VNPAY.
     * Validates the checksum and updates the order/payment status.
     * Returns true if payment was successful, false otherwise.
     */
    boolean processPaymentReturn(HttpServletRequest request);
}
