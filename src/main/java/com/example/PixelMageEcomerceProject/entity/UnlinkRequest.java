package com.example.PixelMageEcomerceProject.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.PixelMageEcomerceProject.enums.UnlinkRequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a customer's request to unlink an NFC card (TASK-05).
 *
 * Flow:
 *   PENDING_EMAIL → (customer clicks email link) → EMAIL_CONFIRMED
 *   EMAIL_CONFIRMED → Staff approve → APPROVED  (unlinkCard() chain fires)
 *   EMAIL_CONFIRMED → Staff reject  → REJECTED  (customer notified)
 *
 * Token: UUID, 10-minute expiry. Expired token → 410 Gone (TokenExpiredException).
 * Customer cannot self-unlink — policy frozen (per AGENTS.md).
 */
@Entity
@Table(name = "unlink_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UnlinkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account customer;

    @Column(name = "nfc_uid", nullable = false, length = 100)
    private String nfcUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UnlinkRequestStatus status;

    /** UUID random — single-use, 10-minute expiry. */
    @Column(name = "token", nullable = false, length = 36, unique = true)
    private String token;

    @Column(name = "token_expiry", nullable = false)
    private LocalDateTime tokenExpiry;

    /** Staff reason when rejecting (nullable). */
    @Column(name = "staff_note", length = 500)
    private String staffNote;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Set when Staff approves or rejects the request. */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
