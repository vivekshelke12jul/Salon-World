package com.vivek.reviewMicroservice.payload.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String fullName;
    private String email;

}
