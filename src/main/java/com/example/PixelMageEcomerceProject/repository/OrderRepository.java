package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.Order;
import com.example.PixelMageEcomerceProject.enums.OrderStatus;
import com.example.PixelMageEcomerceProject.enums.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll();

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByAccountCustomerId(Integer customerId);

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByStatus(OrderStatus status);

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByPaymentStatus(PaymentStatus status);

    @EntityGraph(value = "Order.withDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findByPaymentStatusAndOrderDateAfter(PaymentStatus status, LocalDateTime date);
}
