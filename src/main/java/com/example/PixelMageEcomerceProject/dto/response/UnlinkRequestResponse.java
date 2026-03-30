package com.example.PixelMageEcomerceProject.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for UnlinkRequest (TASK-05).
 * Never exposes raw entity — follows Controller pattern rule.
 */
public record UnlinkRequestResponse(
        Long id,
        String nfcUid,
        String status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String staffNote
) {}
