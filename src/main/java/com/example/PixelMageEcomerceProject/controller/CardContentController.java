package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.CardContentRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.CardContent;
import com.example.PixelMageEcomerceProject.service.interfaces.CardContentService;
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
@RequestMapping("/api/card-contents")
@RequiredArgsConstructor
@Tag(name = "Card Content Management", description = "APIs for managing card contents")
@SecurityRequirement(name = "bearerAuth")
public class CardContentController {

    private final CardContentService cardContentService;

    @PostMapping
    @Operation(summary = "Create a new card content", description = "Create a new card content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card content created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createCardContent(@RequestBody CardContentRequestDTO cardContentRequestDTO) {
        try {
            CardContent createdContent = cardContentService.createCardContent(cardContentRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Card content created successfully",
                    createdContent
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create card content: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all card contents", description = "Retrieve all card contents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card contents retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllCardContents() {
        List<CardContent> contents = cardContentService.getAllCardContents();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Card contents retrieved successfully",
                contents
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get card content by ID", description = "Retrieve a card content by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card content found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card content not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCardContentById(@PathVariable Integer id) {
        Optional<CardContent> content = cardContentService.getCardContentById(id);
        if (content.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card content found",
                    content.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Card content not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/card/{cardId}")
    @Operation(summary = "Get card contents by card ID", description = "Retrieve all contents for a specific card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card contents found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCardContentsByCardId(@PathVariable Integer cardId) {
        List<CardContent> contents = cardContentService.getCardContentsByCardId(cardId);
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Card contents retrieved successfully",
                contents
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update card content", description = "Update an existing card content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card content updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card content not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateCardContent(@PathVariable Integer id, @RequestBody CardContentRequestDTO cardContentRequestDTO) {
        try {
            CardContent updatedContent = cardContentService.updateCardContent(id, cardContentRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card content updated successfully",
                    updatedContent
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
    @Operation(summary = "Delete card content", description = "Delete a card content by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card content deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card content not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteCardContent(@PathVariable Integer id) {
        try {
            cardContentService.deleteCardContent(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card content deleted successfully",
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
