package com.example.PixelMageEcomerceProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.dto.request.CardRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Card;
import com.example.PixelMageEcomerceProject.service.interfaces.CardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Card Management", description = "APIs for managing cards")
@SecurityRequirement(name = "bearerAuth")
public class CardController {

        private final CardService cardService;

        @PostMapping("/create")
        @Operation(summary = "Create a new card product (PENDING_BIND)", description = "Create a new card product without NFC bind")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Card created successfully", content = @Content(schema = @Schema(implementation = ResponseBase.class))),
                        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
                try {
                        Card createdCard = cardService.createCardProduct(cardRequestDTO);
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.CREATED.value(),
                                        "Card created successfully",
                                        createdCard);
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } catch (Exception e) {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Failed to create card: " + e.getMessage(),
                                        null);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
        }

        @PostMapping("/bind")
        @Operation(summary = "Bind NFC to a card", description = "Assigns an NFC UID to a card and changes status to READY")
        public ResponseEntity<ResponseBase> bindNFC(@RequestParam Integer cardId, @RequestParam String nfcUid) {
                try {
                        Card card = cardService.bindNFC(cardId, nfcUid);
                        return ResponseEntity
                                        .ok(new ResponseBase(HttpStatus.OK.value(), "NFC bound successfully", card));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                        .body(new ResponseBase(HttpStatus.CONFLICT.value(), e.getMessage(), null));
                }
        }

        @PutMapping("/{id}/status")
        @Operation(summary = "Update card status", description = "Override card status directly")
        public ResponseEntity<ResponseBase> updateStatus(@PathVariable Integer id, @RequestParam String newStatus) {
                try {
                        Card card = cardService.updateStatus(id, newStatus);
                        return ResponseEntity.ok(
                                        new ResponseBase(HttpStatus.OK.value(), "Status updated successfully", card));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(new ResponseBase(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
                }
        }

        @GetMapping("/list")
        @Operation(summary = "Get all cards list", description = "Retrieve all card products")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cards retrieved successfully", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> getAllCards() {
                List<Card> cards = cardService.getAllCards();
                ResponseBase response = new ResponseBase(
                                HttpStatus.OK.value(),
                                "Cards retrieved successfully",
                                cards);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get card by ID", description = "Retrieve a card by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Card found", content = @Content(schema = @Schema(implementation = ResponseBase.class))),
                        @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> getCardById(@PathVariable Integer id) {
                Optional<Card> card = cardService.getCardById(id);
                if (card.isPresent()) {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.OK.value(),
                                        "Card found",
                                        card.get());
                        return ResponseEntity.ok(response);
                } else {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.NOT_FOUND.value(),
                                        "Card not found with id: " + id,
                                        null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
        }

        @GetMapping("/nfc/{nfcUid}")
        @Operation(summary = "Get card by NFC UID", description = "Retrieve a card by its NFC UID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Card found", content = @Content(schema = @Schema(implementation = ResponseBase.class))),
                        @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> getCardByNfcUid(@PathVariable String nfcUid) {
                Optional<Card> card = cardService.getCardByNfcUid(nfcUid);
                if (card.isPresent()) {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.OK.value(),
                                        "Card found",
                                        card.get());
                        return ResponseEntity.ok(response);
                } else {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.NOT_FOUND.value(),
                                        "Card not found with NFC UID: " + nfcUid,
                                        null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update card", description = "Update an existing card")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Card updated successfully", content = @Content(schema = @Schema(implementation = ResponseBase.class))),
                        @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> updateCard(@PathVariable Integer id,
                        @RequestBody CardRequestDTO cardRequestDTO) {
                try {
                        Card updatedCard = cardService.updateCard(id, cardRequestDTO);
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.OK.value(),
                                        "Card updated successfully",
                                        updatedCard);
                        return ResponseEntity.ok(response);
                } catch (RuntimeException e) {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getMessage(),
                                        null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete card", description = "Delete a card by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Card deleted successfully", content = @Content(schema = @Schema(implementation = ResponseBase.class))),
                        @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseBase.class)))
        })
        public ResponseEntity<ResponseBase> deleteCard(@PathVariable Integer id) {
                try {
                        cardService.deleteCard(id);
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.OK.value(),
                                        "Card deleted successfully",
                                        null);
                        return ResponseEntity.ok(response);
                } catch (RuntimeException e) {
                        ResponseBase response = new ResponseBase(
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getMessage(),
                                        null);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
        }
}
