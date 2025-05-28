package com.vivek.notificationMicroservice.payload.dto;


import com.vivek.notificationMicroservice.domain.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {
    private Integer id;

    private Integer salonId;

    private Integer customerId;

    private UserDTO customer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Integer> servicesIds;

    private Set<ServiceOfferingDTO> services;

    private BookingStatus status;

    private int totalPrice;

}
