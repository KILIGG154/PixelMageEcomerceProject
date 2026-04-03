package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cache Management", description = "Internal tools for cache maintenance")
@SecurityRequirement(name = "bearerAuth")
public class CacheController {

    private final RedisConnectionFactory connectionFactory;

    @DeleteMapping("/clear")
    @Operation(summary = "Clear all Redis cache", description = "Flush the entire Redis database. USE WITH CAUTION.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBase<String>> clearCache() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.serverCommands().flushDb();
            log.info("[CACHE] Redis database flushed by admin request");
            return ResponseBase.ok("Redis cache cleared successfully", "OK");
        } catch (Exception e) {
            log.error("[CACHE] Failed to flush Redis database", e);
            throw new RuntimeException("Could not clear cache: " + e.getMessage());
        }
    }
}
