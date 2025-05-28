package com.vivek.reviewMicroservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private Integer salonId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt=LocalDateTime.now();
}
