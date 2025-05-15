package com.vivek.paymentMicroservice.model;

import com.vivek.paymentMicroservice.domain.PaymentMethod;
import com.vivek.paymentMicroservice.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer amount;

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    private String paymentLinkId;


    private Integer userId;

    private Integer bookingId;

    @Column(nullable = false)
    private Integer salonId;
}