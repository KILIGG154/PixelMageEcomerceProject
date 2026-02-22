package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.OrderRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.Order;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.OrderRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.OrderService;
import com.example.PixelMageEcomerceProject.service.interfaces.PaymentService;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final PaymentService paymentService;

    @Override
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        Account account = accountRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + orderRequestDTO.getCustomerId()));

        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(orderRequestDTO.getStatus());
        order.setTotalAmount(orderRequestDTO.getTotalAmount());
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setPaymentStatus(orderRequestDTO.getPaymentStatus());
        order.setNotes(orderRequestDTO.getNotes());

        return orderRepository.save(order);
    }

    @Override
    public Map<String, Object> createOrderWithPayment(OrderRequestDTO orderRequestDTO, String currency) {
        // First create the order
        Order createdOrder = createOrder(orderRequestDTO);
        
        // Then create payment intent for the order
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(
                createdOrder.getOrderId(),
                createdOrder.getTotalAmount(),
                currency != null ? currency : "usd"
        );
        
        // Return combined response
        Map<String, Object> response = new HashMap<>();
        response.put("order", createdOrder);
        response.put("paymentIntent", Map.of(
                "id", paymentIntent.getId(),
                "clientSecret", paymentIntent.getClientSecret(),
                "status", paymentIntent.getStatus()
        ));
        
        return response;
    }

    @Override
    public Order updateOrder(Integer id, OrderRequestDTO orderRequestDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();

            if (orderRequestDTO.getCustomerId() != null) {
                Account account = accountRepository.findById(orderRequestDTO.getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Account not found with id: " + orderRequestDTO.getCustomerId()));
                updatedOrder.setAccount(account);
            }

            updatedOrder.setOrderDate(orderRequestDTO.getOrderDate());
            updatedOrder.setStatus(orderRequestDTO.getStatus());
            updatedOrder.setTotalAmount(orderRequestDTO.getTotalAmount());
            updatedOrder.setShippingAddress(orderRequestDTO.getShippingAddress());
            updatedOrder.setPaymentMethod(orderRequestDTO.getPaymentMethod());
            updatedOrder.setPaymentStatus(orderRequestDTO.getPaymentStatus());
            updatedOrder.setNotes(orderRequestDTO.getNotes());
            return orderRepository.save(updatedOrder);
        }
        throw new RuntimeException("Order not found with id: " + id);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByAccountCustomerId(customerId);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
