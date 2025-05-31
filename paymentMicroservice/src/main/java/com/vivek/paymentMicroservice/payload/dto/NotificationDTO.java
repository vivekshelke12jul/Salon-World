package com.vivek.paymentMicroservice.payload.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Integer id;
    private String type;
    private Boolean isRead= false;
    private String description;
    private Integer userId;
    private Integer bookingId;
    private Integer salonId;
    private BookingDTO booking;
}

