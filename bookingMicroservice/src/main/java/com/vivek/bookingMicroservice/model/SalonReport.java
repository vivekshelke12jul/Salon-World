package com.vivek.bookingMicroservice.model;

import lombok.Data;

@Data
public class SalonReport {
    private Integer salonId;
    private String salonName;
    private Double totalEarnings;
    private Integer totalBookings;
    private Integer cancelledBookings;
    private Double totalRefund;


}
