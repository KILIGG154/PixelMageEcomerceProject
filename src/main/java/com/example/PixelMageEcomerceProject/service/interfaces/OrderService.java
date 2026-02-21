package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.OrderRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    Order createOrder(OrderRequestDTO orderRequestDTO);
    Order updateOrder(Integer id, OrderRequestDTO orderRequestDTO);
    void deleteOrder(Integer id);
    Optional<Order> getOrderById(Integer id);
    List<Order> getAllOrders();
    List<Order> getOrdersByCustomerId(Integer customerId);
    List<Order> getOrdersByStatus(String status);
}
