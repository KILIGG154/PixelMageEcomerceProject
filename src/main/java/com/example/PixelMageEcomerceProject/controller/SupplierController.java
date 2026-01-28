package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.SupplierRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Supplier;
import com.example.PixelMageEcomerceProject.service.interfaces.SupplierService;
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
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management", description = "APIs for managing suppliers")
@SecurityRequirement(name = "bearerAuth")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create a new supplier", description = "Create a new supplier with name, contact person, email, phone and address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Email already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createSupplier(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Supplier details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SupplierRequestDTO.class))
            )
            @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            Supplier createdSupplier = supplierService.createSupplier(supplierRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Supplier created successfully",
                    createdSupplier
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Suppliers retrieved successfully",
                suppliers
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID", description = "Retrieve supplier details by supplier ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getSupplierById(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable Integer id) {
        return supplierService.getSupplierById(id)
                .map(supplier -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Supplier retrieved successfully",
                            supplier
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Supplier not found with id: " + id,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get supplier by email", description = "Retrieve supplier details by email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getSupplierByEmail(
            @Parameter(description = "Email address", required = true)
            @PathVariable String email) {
        return supplierService.getSupplierByEmail(email)
                .map(supplier -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Supplier retrieved successfully",
                            supplier
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Supplier not found with email: " + email,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get supplier by name", description = "Retrieve supplier details by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getSupplierByName(
            @Parameter(description = "Supplier name", required = true)
            @PathVariable String name) {
        return supplierService.getSupplierByName(name)
                .map(supplier -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.OK.value(),
                            "Supplier retrieved successfully",
                            supplier
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                            HttpStatus.NOT_FOUND.value(),
                            "Supplier not found with name: " + name,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier", description = "Update existing supplier information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or email already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateSupplier(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated supplier details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SupplierRequestDTO.class))
            )
            @RequestBody SupplierRequestDTO supplierRequestDTO) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Supplier updated successfully",
                    updatedSupplier
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to update supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supplier", description = "Delete a supplier by supplier ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteSupplier(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable Integer id) {
        try {
            supplierService.deleteSupplier(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.NO_CONTENT.value(),
                    "Supplier deleted successfully",
                    null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete supplier: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/exists/{email}")
    @Operation(summary = "Check if email exists", description = "Check if an email address is already registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check completed",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> checkEmailExists(
            @Parameter(description = "Email address to check", required = true)
            @PathVariable String email) {
        boolean exists = supplierService.existsByEmail(email);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Email check completed",
                exists
        );
        return ResponseEntity.ok(response);
    }
}

