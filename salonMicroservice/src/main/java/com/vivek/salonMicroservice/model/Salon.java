package com.vivek.salonMicroservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 50)
    private String city;

//    @Column(nullable = false)
//    private boolean homeService;


    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, length = 50)
    private boolean isOpen;

    @Column(nullable = false)
    private Integer ownerId;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @ElementCollection
    private List<String> images;

    public Salon(String name, String email, String phoneNumber, String address, String city, boolean active, boolean isOpen, Integer ownerId, LocalTime openTime, LocalTime closeTime, List<String> images) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;

        this.active = active;
        this.isOpen = isOpen;
        this.ownerId = ownerId;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.images = images;
    }
}

