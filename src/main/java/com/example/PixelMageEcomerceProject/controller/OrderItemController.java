package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.OrderItemRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.OrderItem;
import com.example.PixelMageEcomerceProject.service.interfaces.OrderItemService;
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
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Tag(name = "Order Item Management", description = "APIs for managing order items")
@SecurityRequirement(name = "bearerAuth")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    @Operation(summary = "Create a new order item", description = "Create a new order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createOrderItem(@RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        try {
            OrderItem createdOrderItem = orderItemService.createOrderItem(orderItemRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Order item created successfully",
                    createdOrderItem
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create order item: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all order items", description = "Retrieve all order items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Order items retrieved successfully",
                orderItems
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order item by ID", description = "Retrieve an order item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrderItemById(@PathVariable Integer id) {
        Optional<OrderItem> orderItem = orderItemService.getOrderItemById(id);
        if (orderItem.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order item found",
                    orderItem.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Order item not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get order items by order ID", description = "Retrieve all items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Order items retrieved successfully",
                orderItems
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/card/{cardId}")
    @Operation(summary = "Get order items by card ID", description = "Retrieve all order items for a specific card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOrderItemsByCardId(@PathVariable Integer cardId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByCardId(cardId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Order items retrieved successfully",
                orderItems
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order item", description = "Update an existing order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        try {
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItemRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order item updated successfully",
                    updatedOrderItem
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
    @Operation(summary = "Delete order item", description = "Delete an order item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteOrderItem(@PathVariable Integer id) {
        try {
            orderItemService.deleteOrderItem(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Order item deleted successfully",
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
