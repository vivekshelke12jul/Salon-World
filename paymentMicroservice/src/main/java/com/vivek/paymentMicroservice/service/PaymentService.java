package com.vivek.paymentMicroservice.service;


import com.razorpay.PaymentLink;
import com.vivek.paymentMicroservice.domain.PaymentMethod;
import com.vivek.paymentMicroservice.model.PaymentOrder;
import com.vivek.paymentMicroservice.payload.dto.BookingDTO;
import com.vivek.paymentMicroservice.payload.dto.UserDTO;
import com.vivek.paymentMicroservice.payload.response.PaymentLinkResponse;

public interface PaymentService {

    PaymentLinkResponse createOrder(
            UserDTO user,
            BookingDTO booking,
            PaymentMethod paymentMethod
    ) throws Exception;

    PaymentOrder getPaymentOrderById(Integer id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;

    Boolean ProceedPaymentOrder (
            PaymentOrder paymentOrder,
            String paymentId,
            String paymentLinkId
    ) throws Exception;

    PaymentLink createRazorpayPaymentLink(
            UserDTO user,
            Integer Amount,
            Integer orderId
    ) throws Exception;

    String createStripePaymentLink(
            UserDTO user,
            Integer Amount,
            Integer orderId
    ) throws Exception;
}
