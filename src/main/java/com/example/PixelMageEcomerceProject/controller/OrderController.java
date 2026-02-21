package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.OrderRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Order;
import com.example.PixelMageEcomerceProject.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            Order createdOrder = orderService.createOrder(orderRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Order created successfully",
                    createdOrder
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create order: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Orders retrieved successfully",
                orders
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order found",
                    order.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Order not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer ID", description = "Retrieve all orders for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrdersByCustomerId(@PathVariable Integer customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Orders retrieved successfully",
                orders
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve all orders with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Orders retrieved successfully",
                orders
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Update an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateOrder(@PathVariable Integer id, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order updated successfully",
                    updatedOrder
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Delete an order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteOrder(@PathVariable Integer id) {
        try {
            orderService.deleteOrder(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
