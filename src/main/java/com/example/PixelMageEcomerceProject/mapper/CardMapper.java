package com.example.PixelMageEcomerceProject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.PixelMageEcomerceProject.dto.request.CardRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.PhysicalCardFullResponse;
import com.example.PixelMageEcomerceProject.dto.response.PhysicalCardPublicResponse;
import com.example.PixelMageEcomerceProject.entity.Card;

/**
 * MapStruct 1.5x+ Mapper for Card entity
 * Provides both FULL (Admin) and PUBLIC (User) response mappings
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    // ==================== ENTITY CREATION (Admin only) ====================

    @Mapping(target = "cardTemplate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "nfcUid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Card toEntity(CardRequestDTO dto);

    // ==================== FULL RESPONSE (Admin - All fields) ====================

    @Mapping(source = "cardId", target = "id")
    @Mapping(source = "nfcUid", target = "nfcUid")
    @Mapping(source = "softwareUuid", target = "softwareUuid")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "cardCondition", target = "cardCondition")
    @Mapping(source = "serialNumber", target = "serialNumber")
    @Mapping(source = "productionBatch", target = "productionBatch")
    @Mapping(source = "customText", target = "customText")
    @Mapping(source = "linkedAt", target = "linkedAt")
    @Mapping(source = "soldAt", target = "soldAt")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    // Nested mappings
    @Mapping(source = "cardTemplate.cardTemplateId", target = "cardTemplateId")
    @Mapping(source = "cardTemplate.name", target = "cardTemplateName")
    @Mapping(source = "cardTemplate.imagePath", target = "cardTemplateImageUrl")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "owner.customerId", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    PhysicalCardFullResponse toFullResponse(Card card);

    List<PhysicalCardFullResponse> toFullResponseList(List<Card> cards);

    // ==================== PUBLIC RESPONSE (User - Safe fields only)
    // ====================

    @Mapping(source = "cardId", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "cardCondition", target = "cardCondition")
    @Mapping(source = "linkedAt", target = "linkedAt")
    // Nested card template info (public)
    @Mapping(source = "cardTemplate.name", target = "cardTemplateName")
    @Mapping(source = "cardTemplate.imagePath", target = "cardTemplateImageUrl")
    @Mapping(source = "cardTemplate.description", target = "cardTemplateDescription")
    // Computed field
    @Mapping(target = "isLinked", expression = "java(card.getOwner() != null)")
    PhysicalCardPublicResponse toPublicResponse(Card card);

    List<PhysicalCardPublicResponse> toPublicResponseList(List<Card> cards);
}
