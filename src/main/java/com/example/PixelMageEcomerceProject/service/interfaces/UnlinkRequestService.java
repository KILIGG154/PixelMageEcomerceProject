package com.example.PixelMageEcomerceProject.service.interfaces;

import java.util.List;

import com.example.PixelMageEcomerceProject.dto.response.UnlinkRequestResponse;

/**
 * Service interface for the UnlinkRequest flow (TASK-05).
 *
 * Customer routes:
 *   createRequest  — Customer initiates request; email verification sent.
 *   verifyToken    — Customer clicks email link; status → EMAIL_CONFIRMED.
 *
 * Staff routes:
 *   getQueueForStaff — lists EMAIL_CONFIRMED requests.
 *   approve          — executes full unlinkCard() chain.
 *   reject           — notifies customer via email.
 */
public interface UnlinkRequestService {

    UnlinkRequestResponse createRequest(Integer customerId, String nfcUid);

    void verifyToken(String token);

    List<UnlinkRequestResponse> getQueueForStaff();

    void approve(Long requestId);

    void reject(Long requestId, String staffNote);
}
