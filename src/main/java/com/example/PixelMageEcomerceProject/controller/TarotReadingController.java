package com.example.PixelMageEcomerceProject.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.PixelMageEcomerceProject.entity.Spread;

@RestController
@RequestMapping("/api/v1/readings")
@CrossOrigin("*") // TODO: Update CORS policy for production
public class TarotReadingController {

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
        // TODO: Map with Service & Repository later
        List<Spread> mockSpreads = new ArrayList<>();
        // Mock data to unblock frontend
        Spread s1 = new Spread();
        s1.setSpreadId(1);
        s1.setName("Trải bài 1 lá");
        s1.setDescription("Câu hỏi đơn giản, thông điệp ngày, định hướng nhanh");
        s1.setPositionCount(1);
        mockSpreads.add(s1);

        Spread s2 = new Spread();
        s2.setSpreadId(2);
        s2.setName("Quá khứ - Hiện tại - Tương lai");
        s2.setDescription("Khám phá dòng chảy thời gian của sự việc");
        s2.setPositionCount(3);
        mockSpreads.add(s2);

        return response(true, "Success", mockSpreads);
    }

    /**
     * Bắt đầu một phiên bốc bài mới
     */
    @PostMapping("/sessions")
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody Map<String, Object> payload) {
        // TODO: Receive spreadId and mainQuestion from payload
        // Create session mapped to current User ID
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("sessionId", 101);
        mockData.put("status", "PENDING");
        return response(true, "Session created successfully", mockData);
    }

    /**
     * Người dùng lật bài (Gán thẻ bài vào vị trí)
     */
    @PostMapping("/sessions/{id}/draw")
    public ResponseEntity<Map<String, Object>> drawCards(@PathVariable("id") Integer sessionId,
            @RequestBody Map<String, Object> payload) {
        // TODO: Receive card templates list and positions
        // Validate with Session state
        return response(true, "Cards drawn and saved to session", null);
    }

    /**
     * Gọi trigger giải bài từ n8n/AI
     */
    @GetMapping("/sessions/{id}/interpret")
    public ResponseEntity<Map<String, Object>> interpretSession(@PathVariable("id") Integer sessionId) {
        // TODO: Implement Logic calls n8n or LLM stream processing here
        Map<String, Object> mockData = new HashMap<>();
        mockData.put("aiInterpretation",
                "Lá bài bạn bốc được là The Fool. Hãy can đảm bước đi trên hành trình mới của bạn...");
        return response(true, "Success", mockData);
    }
}
