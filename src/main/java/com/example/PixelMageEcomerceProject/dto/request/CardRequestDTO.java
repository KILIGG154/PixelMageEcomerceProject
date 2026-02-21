package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDTO {
    private String nfcUuid;
    private Integer cardTemplateId;
    private Integer productId;
    private String customText;
}
