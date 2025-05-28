package com.vivek.reviewMicroservice.payload.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {

    private Integer id;

    private Integer property;

    private UserDTO user;

    private SalonDTO salon;

    private String reviewText;

    private double rating;

    private LocalDateTime createdAt;
}
