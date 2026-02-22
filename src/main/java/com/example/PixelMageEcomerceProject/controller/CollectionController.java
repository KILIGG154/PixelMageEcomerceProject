package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.CollectionItemRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.CollectionRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.entity.CardCollection;
import com.example.PixelMageEcomerceProject.entity.CollectionItem;
import com.example.PixelMageEcomerceProject.service.interfaces.CollectionService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@Tag(name = "Collection Management", description = "APIs for managing card collections (bộ sưu tập thẻ)")
@SecurityRequirement(name = "bearerAuth")
public class CollectionController {

    private final CollectionService collectionService;

    // ==================== Collection CRUD ====================

    @PostMapping("/{customerId}")
    @Operation(summary = "Create a new collection", description = "Create a new card collection for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collection created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createCollection(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @RequestBody CollectionRequestDTO request) {
        try {
            CardCollection collection = collectionService.createCollection(customerId, request);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Collection created successfully",
                    collection
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create collection: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{customerId}/{collectionId}")
    @Operation(summary = "Update a collection", description = "Update collection name, description, or visibility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collection updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Collection not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateCollection(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @Parameter(description = "Collection ID") @PathVariable Integer collectionId,
            @RequestBody CollectionRequestDTO request) {
        try {
            CardCollection collection = collectionService.updateCollection(customerId, collectionId, request);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Collection updated successfully",
                    collection
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to update collection: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{customerId}/{collectionId}")
    @Operation(summary = "Delete a collection", description = "Delete a collection and all its items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collection deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Collection not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteCollection(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @Parameter(description = "Collection ID") @PathVariable Integer collectionId) {
        try {
            collectionService.deleteCollection(customerId, collectionId);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Collection deleted successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete collection: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{collectionId}")
    @Operation(summary = "Get collection by ID", description = "Retrieve a specific collection with its items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collection found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Collection not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCollectionById(
            @Parameter(description = "Collection ID") @PathVariable Integer collectionId) {
        Optional<CardCollection> collection = collectionService.getCollectionById(collectionId);
        if (collection.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Collection retrieved successfully",
                    collection.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Collection not found with id: " + collectionId,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer's collections", description = "Retrieve all collections for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collections retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCollectionsByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId) {
        List<CardCollection> collections = collectionService.getCollectionsByCustomerId(customerId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Collections retrieved successfully",
                collections
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    @Operation(summary = "Get public collections", description = "Retrieve all public collections from all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Public collections retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getPublicCollections() {
        List<CardCollection> collections = collectionService.getPublicCollections();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Public collections retrieved successfully",
                collections
        );
        return ResponseEntity.ok(response);
    }

    // ==================== Collection Items ====================

    @PostMapping("/items/{customerId}")
    @Operation(summary = "Add card to collection",
            description = "Add an owned card to a collection. Card must be purchased (order COMPLETED & PAID) before adding.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card added to collection successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Card not owned or already in collection",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> addCardToCollection(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @RequestBody CollectionItemRequestDTO request) {
        try {
            CollectionItem item = collectionService.addCardToCollection(customerId, request);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Card added to collection successfully",
                    item
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to add card to collection: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/items/{customerId}/{collectionId}/{cardId}")
    @Operation(summary = "Remove card from collection", description = "Remove a card from a collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card removed from collection successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card not found in collection",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> removeCardFromCollection(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @Parameter(description = "Collection ID") @PathVariable Integer collectionId,
            @Parameter(description = "Card ID") @PathVariable Integer cardId) {
        try {
            collectionService.removeCardFromCollection(customerId, collectionId, cardId);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card removed from collection successfully",
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to remove card: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/items/{collectionId}")
    @Operation(summary = "Get collection items", description = "Get all cards in a collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Collection items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCollectionItems(
            @Parameter(description = "Collection ID") @PathVariable Integer collectionId) {
        List<CollectionItem> items = collectionService.getCollectionItems(collectionId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Collection items retrieved successfully",
                items
        );
        return ResponseEntity.ok(response);
    }

    // ==================== Owned Cards ====================

    @GetMapping("/owned-cards/{customerId}")
    @Operation(summary = "Get owned cards",
            description = "Get all cards owned by a customer (purchased through COMPLETED & PAID orders)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owned cards retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getOwnedCards(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId) {
        List<Card> ownedCards = collectionService.getOwnedCards(customerId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Owned cards retrieved successfully (" + ownedCards.size() + " cards)",
                ownedCards
        );
        return ResponseEntity.ok(response);
    }
}
