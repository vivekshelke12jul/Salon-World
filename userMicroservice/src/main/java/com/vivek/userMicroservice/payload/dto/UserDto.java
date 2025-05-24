package com.vivek.userMicroservice.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String fullName;
    private String email;
    private String role;
}

