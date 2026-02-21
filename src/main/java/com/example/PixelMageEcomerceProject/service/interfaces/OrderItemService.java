package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.OrderItemRequestDTO;
import com.example.PixelMageEcomerceProject.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderItemService {
    OrderItem createOrderItem(OrderItemRequestDTO orderItemRequestDTO);
    OrderItem updateOrderItem(Integer id, OrderItemRequestDTO orderItemRequestDTO);
    void deleteOrderItem(Integer id);
    Optional<OrderItem> getOrderItemById(Integer id);
    List<OrderItem> getAllOrderItems();
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    List<OrderItem> getOrderItemsByCardId(Integer cardId);
}
