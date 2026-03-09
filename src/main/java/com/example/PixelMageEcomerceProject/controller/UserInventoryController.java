package com.example.PixelMageEcomerceProject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.entity.UserInventory;
import com.example.PixelMageEcomerceProject.service.interfaces.UserInventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class UserInventoryController {

    private final UserInventoryService userInventoryService;

    @GetMapping("/my-cards")
    public ResponseEntity<List<UserInventory>> getMyInventory(@RequestParam("userId") Integer userId) {
        // In a real app, userId should come from the authenticated JWT token context
        List<UserInventory> inventoryList = userInventoryService.getUserInventory(userId);
        return ResponseEntity.ok(inventoryList);
    }

    @GetMapping("/my-cards/{templateId}")
    public ResponseEntity<UserInventory> getMyInventoryByTemplate(
            @RequestParam("userId") Integer userId,
            @PathVariable Integer templateId) {
        // In a real app, userId should come from the authenticated JWT token context
        List<UserInventory> inventoryList = userInventoryService.getUserInventory(userId);
        UserInventory result = inventoryList.stream()
                .filter(inv -> inv.getCardTemplate().getCardTemplateId().equals(templateId))
                .findFirst()
                .orElse(null);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
