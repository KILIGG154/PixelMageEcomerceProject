package com.example.PixelMageEcomerceProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.entity.Spread;
import com.example.PixelMageEcomerceProject.service.interfaces.TarotReadingService;

@RestController
@RequestMapping("/api/v1/readings")
@CrossOrigin("*")
public class TarotReadingController {

    @Autowired
    private TarotReadingService tarotReadingService;

    // Helper method for standard response
    private ResponseEntity<Map<String, Object>> response(boolean success, String message, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", success);
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.ok(body);
    }

    /**
     * Lấy danh sách các kiểu trải bài (Spread)
     */
    @GetMapping("/spreads")
    public ResponseEntity<Map<String, Object>> getSpreads() {
        try {
            List<Spread> spreads = tarotReadingService.getAllSpreads();
            return response(true, "Success", spreads);
        } catch (Exception e) {
            return response(false, e.getMessage(), null);
        }
    }

    /**
     * Bắt đầu một phiên bốc bài mới
     */
    @PostMapping("/sessions")
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody Map<String, Object> payload) {
        try {
            // Hardcode accountId=1 for testing purpose. Usually we get it from JWT Context
            Integer accountId = 1;
            Integer spreadId = (Integer) payload.get("spreadId");
            String mode = (String) payload.get("mode");

            Map<String, Object> result = tarotReadingService.createSession(accountId, spreadId, mode);
            return response(true, "Session created successfully", result);
        } catch (Exception e) {
            return response(false, e.getMessage(), null);
        }
    }

    /**
     * Người dùng lật bài (Gán thẻ bài vào vị trí)
     */
    @PostMapping("/sessions/{id}/draw")
    public ResponseEntity<Map<String, Object>> drawCards(@PathVariable("id") Integer sessionId,
            @RequestBody Map<String, Object> payload) {
        try {
            Boolean allowReversed = (Boolean) payload.getOrDefault("allowReversed", false);
            Map<String, Object> result = tarotReadingService.drawCards(sessionId, allowReversed);
            return response(true, "Cards drawn and saved to session", result);
        } catch (Exception e) {
            return response(false, e.getMessage(), null);
        }
    }

    /**
     * Gọi trigger giải bài từ n8n/AI hoặc Fallback Local
     */
    @GetMapping("/sessions/{id}/interpret")
    public ResponseEntity<Map<String, Object>> interpretSession(@PathVariable("id") Integer sessionId) {
        try {
            Map<String, Object> result = tarotReadingService.interpretSession(sessionId);
            return response(true, "Interpretation generated", result);
        } catch (Exception e) {
            return response(false, e.getMessage(), null);
        }
    }
}
