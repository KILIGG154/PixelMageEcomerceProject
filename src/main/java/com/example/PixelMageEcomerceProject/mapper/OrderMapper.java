package com.example.PixelMageEcomerceProject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.PixelMageEcomerceProject.dto.request.OrderRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.OrderResponse;
import com.example.PixelMageEcomerceProject.entity.Order;
import com.example.PixelMageEcomerceProject.entity.Voucher;

@Mapper(componentModel = "spring", uses = { AccountMapper.class, ProductMapper.class, OrderItemMapper.class })
public interface OrderMapper {

    @Mapping(target = "customer", source = "account")
    @Mapping(target = "appliedVoucher", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "finalAmount", ignore = true)
    @Mapping(target = "paymentQrUrl", ignore = true)
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "account", ignore = true) // Set in service
    @Mapping(target = "orderItems", ignore = true) // Handle separately in service
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Order toEntity(OrderRequestDTO dto);

    OrderResponse.AppliedVoucher toAppliedVoucherResponse(Voucher voucher);

    // delegated to OrderItemMapper
    // OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
