package com.vivek.notificationMicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String type;
    private Boolean isRead= false;
    private String description;
    private Integer userId;
    private Integer bookingId;
    private Integer salonId;
    private LocalDateTime createdAt;
}
