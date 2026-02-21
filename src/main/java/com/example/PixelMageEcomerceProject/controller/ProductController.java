package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.dto.request.ProductRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Product;
import com.example.PixelMageEcomerceProject.service.interfaces.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        try {
            Product createdProduct = productService.createProduct(productRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Product created successfully",
                    createdProduct
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create product: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Products retrieved successfully",
                products
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Product found",
                    product.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Product not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateProduct(@PathVariable Integer id, @RequestBody ProductRequestDTO productRequestDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Product updated successfully",
                    updatedProduct
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
    @Operation(summary = "Delete product", description = "Delete a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Product deleted successfully",
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
