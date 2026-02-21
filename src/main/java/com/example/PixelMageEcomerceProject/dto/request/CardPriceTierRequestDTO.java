package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPriceTierRequestDTO {
    private Integer cardTemplateId;
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal pricePerUnit;
}

