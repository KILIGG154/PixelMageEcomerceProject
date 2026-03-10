package com.example.PixelMageEcomerceProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.PixelMageEcomerceProject.dto.request.SetStoryRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.SetStory;
import com.example.PixelMageEcomerceProject.service.interfaces.SetStoryService;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Set Story Management", description = "APIs for managing and viewing Set Stories")
@SecurityRequirement(name = "bearerAuth")
public class SetStoryController {

    private final SetStoryService setStoryService;

    @GetMapping("/stories")
    @Operation(summary = "Get all stories with lock status",
            description = "Return list of all stories and whether each one is unlocked for the given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getStories(@RequestParam Integer userId) {
        List<SetStory> allStories = setStoryService.getAllStories();
        List<SetStory> unlocked = setStoryService.getUnlockedStories(userId);

        Map<Integer, Boolean> unlockedMap = new HashMap<>();
        for (SetStory story : unlocked) {
            unlockedMap.put(story.getStoryId(), true);
        }

        List<Map<String, Object>> result = allStories.stream().map(story -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("storyId", story.getStoryId());
            dto.put("title", story.getTitle());
            dto.put("coverImagePath", story.getCoverImagePath());
            dto.put("isActive", story.getIsActive());
            dto.put("unlocked", unlockedMap.getOrDefault(story.getStoryId(), false));
            return dto;
        }).toList();

        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Stories retrieved successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stories/{id}")
    @Operation(summary = "Get story detail",
            description = "Return story content only if the story has been unlocked for the given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "403", description = "Story is locked",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Story not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getStoryDetail(@PathVariable Integer id, @RequestParam Integer userId) {
        SetStory story;
        try {
            story = setStoryService.getStoryById(id);
        } catch (RuntimeException e) {
            ResponseBase resp = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Story not found with id: " + id,
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        boolean unlocked = setStoryService.getUnlockedStories(userId).stream()
                .anyMatch(s -> s.getStoryId().equals(id));

        if (!unlocked) {
            ResponseBase resp = new ResponseBase(
                    HttpStatus.FORBIDDEN.value(),
                    "Story is locked for this user",
                    null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
        }

        ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Story retrieved successfully",
                story);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/stories")
    @Operation(summary = "Create a new Set Story", description = "Admin API to create a Set Story definition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Story created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createStory(@RequestBody SetStoryRequestDTO request) {
        try {
            SetStory story = new SetStory();
            story.setTitle(request.getTitle());
            story.setContent(request.getContent());
            story.setRequiredTemplateIds(request.getRequiredTemplateIds());
            story.setCoverImagePath(request.getCoverImagePath());
            story.setIsActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE);

            SetStory saved = setStoryService.createStory(story);
            ResponseBase response = new ResponseBase(
                    HttpStatus.CREATED.value(),
                    "Story created successfully",
                    saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.BAD_REQUEST.value(),
                    "Failed to create story: " + e.getMessage(),
                    null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/admin/stories/{id}")
    @Operation(summary = "Update Set Story", description = "Admin API to update Set Story definition")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Story not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateStory(@PathVariable Integer id, @RequestBody SetStoryRequestDTO request) {
        try {
            SetStory existing = setStoryService.getStoryById(id);
            existing.setTitle(request.getTitle() != null ? request.getTitle() : existing.getTitle());
            existing.setContent(request.getContent() != null ? request.getContent() : existing.getContent());
            existing.setRequiredTemplateIds(request.getRequiredTemplateIds() != null
                    ? request.getRequiredTemplateIds()
                    : existing.getRequiredTemplateIds());
            existing.setCoverImagePath(request.getCoverImagePath() != null
                    ? request.getCoverImagePath()
                    : existing.getCoverImagePath());
            if (request.getIsActive() != null) {
                existing.setIsActive(request.getIsActive());
            }

            SetStory saved = setStoryService.updateStory(existing);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Story updated successfully",
                    saved);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to update story: " + e.getMessage(),
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/admin/stories/{id}")
    @Operation(summary = "Delete Set Story", description = "Admin API to delete Set Story")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Story deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Story not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteStory(@PathVariable Integer id) {
        try {
            setStoryService.deleteStory(id);
            ResponseBase response = new ResponseBase(
                    HttpStatus.OK.value(),
                    "Story deleted successfully",
                    null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed to delete story: " + e.getMessage(),
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

