package com.vivek.paymentMicroservice.payload.dto;

import com.vivek.paymentMicroservice.domain.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Integer id;

    private Integer salonId;

    private SalonDTO salon;

    private Integer customerId;

    private UserDTO customer;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Set<Integer> servicesIds;

    private Set<ServiceOfferingDTO> services;

    private BookingStatus status;

    private int totalPrice;
}
