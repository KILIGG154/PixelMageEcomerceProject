package com.example.PixelMageEcomerceProject.dto.response;

import java.time.LocalDateTime;

import com.example.PixelMageEcomerceProject.enums.CardCondition;
import com.example.PixelMageEcomerceProject.enums.CardProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Full response for ADMIN - includes all sensitive fields
 * Use case: Admin dashboard, inventory management, physical card monitoring
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalCardFullResponse {
    // Primary identifiers
    private Integer id;
    private String nfcUid;
    private String softwareUuid;

    // Status & Condition
    private CardProductStatus status;
    private CardCondition cardCondition;

    // Card Details
    private Integer cardTemplateId;
    private String cardTemplateName;
    private String cardTemplateImageUrl;
    private Integer productId;
    private String productName;

    // Physical tracking
    private String serialNumber;
    private String productionBatch;
    private String customText;

    // Ownership
    private Integer ownerId;
    private String ownerName;
    private String ownerEmail;

    // Timestamps
    private LocalDateTime linkedAt;
    private LocalDateTime soldAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
