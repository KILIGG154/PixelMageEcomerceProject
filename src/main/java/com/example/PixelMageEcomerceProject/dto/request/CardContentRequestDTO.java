package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardContentRequestDTO {
    private Integer cardId;
    private String contentType;
    private String contentData;
    private String position;
}

