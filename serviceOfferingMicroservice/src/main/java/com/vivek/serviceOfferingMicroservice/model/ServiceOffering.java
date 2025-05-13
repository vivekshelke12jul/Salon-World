package com.vivek.serviceOfferingMicroservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServiceOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int duration;

    private Integer salonId;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private Integer categoryId;


    private String image;


}

