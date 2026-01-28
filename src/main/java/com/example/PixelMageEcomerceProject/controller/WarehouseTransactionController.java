package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.WarehouseTransactionRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.WarehouseTransaction;
import com.example.PixelMageEcomerceProject.service.interfaces.WarehouseTransactionService;
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
@RequestMapping("/api/warehouse-transactions")
@RequiredArgsConstructor
@Tag(name = "Warehouse Transaction Management", description = "APIs for managing warehouse transactions and updating inventory")
@SecurityRequirement(name = "bearerAuth")
public class WarehouseTransactionController {

    private final WarehouseTransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create warehouse transaction",
               description = "Create a new warehouse transaction and automatically update inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully and inventory updated",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WarehouseTransactionRequestDTO.class))
            )
            @RequestBody WarehouseTransactionRequestDTO transactionRequestDTO) {
        try {
            WarehouseTransaction createdTransaction = transactionService.createTransaction(transactionRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Transaction created successfully and inventory updated",
                    createdTransaction
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create transaction: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieve all warehouse transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllTransactions() {
        List<WarehouseTransaction> transactions = transactionService.getAllTransactions();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Transactions retrieved successfully",
                transactions
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a warehouse transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getTransactionById(@PathVariable Integer id) {
        Optional<WarehouseTransaction> transaction = transactionService.getTransactionById(id);
        if (transaction.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Transaction retrieved successfully",
                    transaction.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Transaction not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get transactions by warehouse", description = "Retrieve all transactions for a specific warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getTransactionsByWarehouseId(@PathVariable Integer warehouseId) {
        List<WarehouseTransaction> transactions = transactionService.getTransactionsByWarehouseId(warehouseId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Transactions retrieved successfully",
                transactions
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get transactions by product", description = "Retrieve all transactions for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getTransactionsByProductId(@PathVariable Integer productId) {
        List<WarehouseTransaction> transactions = transactionService.getTransactionsByProductId(productId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Transactions retrieved successfully",
                transactions
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction", description = "Update an existing warehouse transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateTransaction(
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction details to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WarehouseTransactionRequestDTO.class))
            )
            @RequestBody WarehouseTransactionRequestDTO transactionRequestDTO) {
        try {
            WarehouseTransaction updatedTransaction = transactionService.updateTransaction(id, transactionRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Transaction updated successfully",
                    updatedTransaction
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to update transaction: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a warehouse transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteTransaction(@PathVariable Integer id) {
        try {
            transactionService.deleteTransaction(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Transaction deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete transaction: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

