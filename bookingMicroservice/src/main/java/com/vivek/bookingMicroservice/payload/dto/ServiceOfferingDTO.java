package com.vivek.bookingMicroservice.payload.dto;

import lombok.Data;

@Data
public class ServiceOfferingDTO {


    private Integer id;
    private String name;
    private String description;
    private int price;
    private int duration;
    private Integer salon;
    private boolean available;
    private Integer category;
    private String image;
}
