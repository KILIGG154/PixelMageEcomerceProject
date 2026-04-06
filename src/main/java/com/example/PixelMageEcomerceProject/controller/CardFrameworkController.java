package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.response.CardFrameworkResponse;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.repository.CardFrameworkRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/card-frameworks")
@RequiredArgsConstructor
@Tag(name = "Card Framework Management", description = "Read endpoints for CardFramework master data")
public class CardFrameworkController {

    private final CardFrameworkRepository cardFrameworkRepository;

    @GetMapping
    @Operation(
            summary = "Get all active CardFrameworks (Public)",
            description = "Used by Admin/Staff UI to populate CardTemplate create/edit framework selector."
    )
    public ResponseEntity<ResponseBase<List<CardFrameworkResponse>>> getActiveFrameworks() {
        List<CardFrameworkResponse> list = cardFrameworkRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(f -> CardFrameworkResponse.builder()
                        .frameworkId(f.getFrameworkId())
                        .name(f.getName())
                        .description(f.getDescription())
                        .active(f.getActive())
                        .build())
                .toList();
        return ResponseBase.ok(list, "Card frameworks retrieved successfully");
    }
}

