package com.example.PixelMageEcomerceProject.dto.response;

import java.time.LocalDateTime;

import com.example.PixelMageEcomerceProject.enums.CardCondition;
import com.example.PixelMageEcomerceProject.enums.CardProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Public response for USER - hides sensitive fields
 * Use case: User viewing their own cards, card gallery, public card info
 * Security: Excludes nfcUid, softwareUuid, serialNumber, productionBatch, owner
 * details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalCardPublicResponse {
    // Public identifier only
    private Integer id;

    // Status & Condition (public info)
    private CardProductStatus status;
    private CardCondition cardCondition;

    // Card Template Info (what card this is)
    private String cardTemplateName;
    private String cardTemplateImageUrl;
    private String cardTemplateDescription;

    // Only show linkedAt if user owns this card (checked in service layer)
    private LocalDateTime linkedAt;

    // Ownership indicator (anonymous)
    private Boolean isLinked;
}
