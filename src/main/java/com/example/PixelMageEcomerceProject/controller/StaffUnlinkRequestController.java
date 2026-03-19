package com.example.PixelMageEcomerceProject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.dto.request.RejectUnlinkRequest;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.dto.response.UnlinkRequestResponse;
import com.example.PixelMageEcomerceProject.service.interfaces.UnlinkRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Staff-facing endpoints for the UnlinkRequest queue (TASK-05).
 * All endpoints require STAFF or ADMIN role (class-level @PreAuthorize).
 *
 * GET  /api/staff/unlink-requests          — view EMAIL_CONFIRMED queue
 * POST /api/staff/unlink-requests/{id}/approve — execute unlink + revoke chain
 * POST /api/staff/unlink-requests/{id}/reject  — notify customer
 */
@RestController
@RequestMapping("/api/staff/unlink-requests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Staff — Unlink Request", description = "Staff queue management for unlink requests")
public class StaffUnlinkRequestController {

    private final UnlinkRequestService unlinkRequestService;

    @GetMapping
    @Operation(summary = "Get unlink request queue",
               description = "Returns all EMAIL_CONFIRMED requests awaiting Staff action, newest first.")
    public ResponseEntity<ResponseBase<List<UnlinkRequestResponse>>> getQueue() {
        return ResponseBase.ok(unlinkRequestService.getQueueForStaff(), "Danh sách yêu cầu hủy liên kết");
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve unlink request",
               description = "Executes the full unlinkCard() chain: inventory -1, story revoke, achievement revoke. Customer is notified via email.")
    public ResponseEntity<ResponseBase<Void>> approve(@PathVariable Long id) {
        unlinkRequestService.approve(id);
        return ResponseBase.success("Yêu cầu đã được duyệt. Thẻ đã được hủy liên kết.");
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject unlink request",
               description = "Rejects the request with an optional staff note. Customer is notified via email.")
    public ResponseEntity<ResponseBase<Void>> reject(@PathVariable Long id,
                                                      @RequestBody RejectUnlinkRequest req) {
        unlinkRequestService.reject(id, req.staffNote());
        return ResponseBase.success("Yêu cầu đã bị từ chối. Khách hàng đã được thông báo.");
    }
}
