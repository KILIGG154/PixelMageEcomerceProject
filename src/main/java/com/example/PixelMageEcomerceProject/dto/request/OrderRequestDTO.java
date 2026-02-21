package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Integer customerId;
    private LocalDateTime orderDate;
    private String status; // PENDING, PROCESSING, COMPLETED, CANCELLED
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED
    private String notes;
    private List<OrderItemRequestDTO> orderItems;
}

