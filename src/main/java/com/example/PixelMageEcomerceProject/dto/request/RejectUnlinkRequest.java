package com.example.PixelMageEcomerceProject.dto.request;

/**
 * Request body for POST /api/staff/unlink-requests/{id}/reject (TASK-05).
 */
public record RejectUnlinkRequest(String staffNote) {}
