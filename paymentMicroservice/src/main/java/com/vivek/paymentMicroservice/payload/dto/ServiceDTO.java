package com.vivek.paymentMicroservice.payload.dto;

import lombok.Data;

@Data
public class ServiceDTO {

    private String name;
    private String description;
    private int price;
    private int duration;
    private Integer category;
    private String image;
}
