package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderLineRequest;
import com.example.PixelMageEcomerceProject.dto.request.PurchaseOrderRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.PurchaseOrder;
import com.example.PixelMageEcomerceProject.service.interfaces.PurchaseOrderLineService;
import com.example.PixelMageEcomerceProject.service.interfaces.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Order Management", description = "APIs for managing purchase orders")
@SecurityRequirement(name = "bearerAuth")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    private final PurchaseOrderLineService purchaseOrderLineService;

    @PostMapping
    @Operation(summary = "Create a new purchase order", description = "Create a new purchase order with warehouse, supplier, PO number, status, order date and expected delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase order created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - PO number already exists or supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createPurchaseOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Purchase order details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PurchaseOrderRequestDTO.class))
            )
            @RequestBody PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        try {
            PurchaseOrder createdPO = purchaseOrderService.createPurchaseOrder(purchaseOrderRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Purchase order created successfully",
                    createdPO
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create purchase order: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all purchase orders", description = "Retrieve a list of all purchase orders in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Purchase orders retrieved successfully",
                purchaseOrders
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID", description = "Retrieve purchase order details by PO ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getPurchaseOrderById(
            @Parameter(description = "Purchase Order ID", required = true)
            @PathVariable Integer id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(po -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Purchase order retrieved successfully",
                            po
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Purchase order not found with id: " + id,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/po-number/{poNumber}")
    @Operation(summary = "Get purchase order by PO number", description = "Retrieve purchase order details by PO number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getPurchaseOrderByPoNumber(
            @Parameter(description = "PO Number", required = true)
            @PathVariable String poNumber) {
        return purchaseOrderService.getPurchaseOrderByPoNumber(poNumber)
                .map(po -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Purchase order retrieved successfully",
                            po
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Purchase order not found with PO number: " + poNumber,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get purchase orders by status", description = "Retrieve purchase orders by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getPurchaseOrdersByStatus(
            @Parameter(description = "Status", required = true)
            @PathVariable String status) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrdersByStatus(status);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Purchase orders retrieved successfully",
                purchaseOrders
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get purchase orders by supplier ID", description = "Retrieve purchase orders by supplier ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getPurchaseOrdersBySupplierId(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable Integer supplierId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrdersBySupplierId(supplierId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Purchase orders retrieved successfully",
                purchaseOrders
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update purchase order", description = "Update existing purchase order information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or PO number already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updatePurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated purchase order details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PurchaseOrderRequestDTO.class))
            )
            @RequestBody PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        try {
            PurchaseOrder updatedPO = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Purchase order updated successfully",
                    updatedPO
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to update purchase order: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete purchase order", description = "Delete a purchase order by PO ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Purchase order deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deletePurchaseOrder(
            @Parameter(description = "Purchase Order ID", required = true)
            @PathVariable Integer id) {
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.NO_CONTENT.value(),
                    "Purchase order deleted successfully",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete purchase order: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/exists/{poNumber}")
    @Operation(summary = "Check if PO number exists", description = "Check if a PO number is already registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PO number check completed",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> checkPoNumberExists(
            @Parameter(description = "PO number to check", required = true)
            @PathVariable String poNumber) {
        boolean exists = purchaseOrderService.existsByPoNumber(poNumber);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "PO number check completed",
                exists
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{poId}/lines")
    @Operation(summary = "Add purchase order line to a purchase order", description = "Add a new purchase order line to an existing purchase order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase order line added successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> addPurchaseOrderLine(@PathVariable int poId, @RequestBody PurchaseOrderLineRequest request){
        try {
            var line = purchaseOrderLineService.createPurchaseOrderLine(request,poId);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Purchase order line added successfully",
                    line
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to add purchase order line: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{poId}/lines/{lineId}")
    @Operation(summary = "Update purchase order line", description = "Update an existing purchase order line")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order line updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Purchase order or line not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updatePurchaseOrderLine( @PathVariable int poId,@PathVariable String lineId){
        try {
            PurchaseOrder updateStatusPo = purchaseOrderService.receivedPurchaseOrder(poId,lineId);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Received full product successfully",
                    updateStatusPo
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Received not enough product: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

