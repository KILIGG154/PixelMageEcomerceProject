package com.example.PixelMageEcomerceProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBase {

    private int code;
    private String message;
    private Object data;
}

