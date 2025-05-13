package com.vivek.bookingMicroservice.payload.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String fullName;
    private String email;

}
