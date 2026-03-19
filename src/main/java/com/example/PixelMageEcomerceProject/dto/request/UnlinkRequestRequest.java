package com.example.PixelMageEcomerceProject.dto.request;

/**
 * Request body for POST /api/unlink-requests (TASK-05).
 * Customer specifies which card (by NFC UID) they want to unlink.
 */
public record UnlinkRequestRequest(String nfcUid) {}
