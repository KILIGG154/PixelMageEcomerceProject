package com.example.PixelMageEcomerceProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private Integer roleId;
}

