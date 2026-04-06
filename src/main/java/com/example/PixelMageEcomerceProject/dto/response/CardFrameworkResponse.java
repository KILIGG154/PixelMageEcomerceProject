package com.example.PixelMageEcomerceProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardFrameworkResponse {
    private Integer frameworkId;
    private String name;
    private String description;
    private Boolean active;
}

