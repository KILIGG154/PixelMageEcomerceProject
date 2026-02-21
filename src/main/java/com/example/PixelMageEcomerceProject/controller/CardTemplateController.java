package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.CardTemplateRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.example.PixelMageEcomerceProject.service.interfaces.CardTemplateService;
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
@RequestMapping("/api/card-templates")
@RequiredArgsConstructor
@Tag(name = "Card Template Management", description = "APIs for managing card templates")
@SecurityRequirement(name = "bearerAuth")
public class CardTemplateController {

    private final CardTemplateService cardTemplateService;

    @PostMapping
    @Operation(summary = "Create a new card template", description = "Create a new card template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card template created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createCardTemplate(@RequestBody CardTemplateRequestDTO cardTemplateRequestDTO) {
        try {
            CardTemplate createdTemplate = cardTemplateService.createCardTemplate(cardTemplateRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Card template created successfully",
                    createdTemplate
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create card template: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all card templates", description = "Retrieve all card templates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card templates retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllCardTemplates() {
        List<CardTemplate> templates = cardTemplateService.getAllCardTemplates();
        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Card templates retrieved successfully",
                templates
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get card template by ID", description = "Retrieve a card template by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card template found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card template not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getCardTemplateById(@PathVariable Integer id) {
        Optional<CardTemplate> template = cardTemplateService.getCardTemplateById(id);
        if (template.isPresent()) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card template found",
                    template.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Card template not found with id: " + id,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update card template", description = "Update an existing card template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card template updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card template not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateCardTemplate(@PathVariable Integer id, @RequestBody CardTemplateRequestDTO cardTemplateRequestDTO) {
        try {
            CardTemplate updatedTemplate = cardTemplateService.updateCardTemplate(id, cardTemplateRequestDTO);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card template updated successfully",
                    updatedTemplate
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
    @Operation(summary = "Delete card template", description = "Delete a card template by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card template deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Card template not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteCardTemplate(@PathVariable Integer id) {
        try {
            cardTemplateService.deleteCardTemplate(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Card template deleted successfully",
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
