package com.example.PixelMageEcomerceProject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.dto.request.UnlinkRequestRequest;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.dto.response.UnlinkRequestResponse;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.service.interfaces.UnlinkRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Customer-facing endpoints for the UnlinkRequest flow (TASK-05).
 *
 * POST /api/unlink-requests        — CUSTOMER only (JWT required).
 * GET  /api/unlink-requests/verify — permit-all (email link click, no Bearer token).
 */
@RestController
@RequestMapping("/api/unlink-requests")
@RequiredArgsConstructor
@Tag(name = "Unlink Request", description = "Customer unlink request flow")
public class UnlinkRequestController {

    private final UnlinkRequestService unlinkRequestService;

    /**
     * Customer tạo yêu cầu hủy liên kết thẻ NFC.
     * Hệ thống gửi email xác nhận ngay sau khi tạo thành công.
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create unlink request",
               description = "Customer creates a request to unlink their NFC card. An email verification link is sent immediately.")
    public ResponseEntity<ResponseBase<UnlinkRequestResponse>> createRequest(
            @RequestBody UnlinkRequestRequest req,
            @AuthenticationPrincipal Account account) {
        return ResponseBase.ok(
                unlinkRequestService.createRequest(account.getCustomerId(), req.nfcUid()),
                "Yêu cầu hủy liên kết đã được tạo. Kiểm tra email để xác nhận."
        );
    }

    /**
     * Customer click link xác nhận từ email.
     * Endpoint này là permit-all (không cần Bearer token) — được whitelist trong SecurityConfig.
     * Token hết hạn (>10 phút) → 410 Gone.
     */
    @GetMapping("/verify")
    @Operation(summary = "Verify unlink email token",
               description = "Public endpoint. Customer clicks email link to confirm request. Token expires in 10 minutes → 410 if expired.")
    public ResponseEntity<ResponseBase<Void>> verifyToken(@RequestParam String token) {
        unlinkRequestService.verifyToken(token);
        return ResponseBase.success("Xác nhận thành công. Yêu cầu đang chờ Staff xét duyệt.");
    }
}
