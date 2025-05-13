package com.vivek.bookingMicroservice.model;

import lombok.Data;

@Data
public class PaymentOrder {
    private Integer id;

    private Integer amount;
//
//    private PaymentOrderStatus status;
//
//    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    private Integer userId;

    private Integer bookingId;
}
