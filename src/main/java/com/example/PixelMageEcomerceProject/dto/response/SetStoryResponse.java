package com.example.PixelMageEcomerceProject.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetStoryResponse {

    private Integer storyId;
    private String title;
    private String content;
    private String requiredTemplateIds;
    private String coverImagePath;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
