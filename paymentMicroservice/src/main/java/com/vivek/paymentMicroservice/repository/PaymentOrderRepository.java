package com.vivek.paymentMicroservice.repository;

import com.vivek.paymentMicroservice.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Integer> {
    Optional<PaymentOrder> findByPaymentLinkId(String paymentId);
}
