package com.example.PixelMageEcomerceProject.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.service.interfaces.NFCScanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/nfc")
@RequiredArgsConstructor
@Tag(name = "NFC Management", description = "APIs for scanning and linking NFC cards")
@SecurityRequirement(name = "bearerAuth")
public class NFCScanController {

    private final NFCScanService nfcScanService;

    @PostMapping("/scan")
    @Operation(summary = "Scan NFC card", description = "Scan an NFC card and return action (LINK_PROMPT or VIEW_CONTENT)")
    public ResponseEntity<ResponseBase> scanNFC(@RequestParam String nfcUid, @RequestParam Integer userId) {
        try {
            Map<String, Object> result = nfcScanService.scanNFC(nfcUid, userId);
            return ResponseEntity.ok(new ResponseBase(HttpStatus.OK.value(), "Scan successful", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseBase(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/link")
    @Operation(summary = "Link NFC card", description = "Link a scanned NFC card to a user account")
    public ResponseEntity<ResponseBase> linkCard(@RequestParam String nfcUid, @RequestParam Integer userId) {
        try {
            Map<String, Object> result = nfcScanService.linkCard(nfcUid, userId);
            return ResponseEntity.ok(new ResponseBase(HttpStatus.OK.value(), "Card linked successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseBase(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/unlink")
    @Operation(summary = "Unlink NFC card", description = "Unlink an NFC card from your account")
    public ResponseEntity<ResponseBase> unlinkCard(@RequestParam String nfcUid, @RequestParam Integer userId) {
        try {
            Map<String, Object> result = nfcScanService.unlinkCard(nfcUid, userId);
            return ResponseEntity.ok(new ResponseBase(HttpStatus.OK.value(), "Card unlinked successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseBase(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }
}
