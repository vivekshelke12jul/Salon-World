package com.vivek.paymentMicroservice.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.vivek.paymentMicroservice.domain.PaymentMethod;
import com.vivek.paymentMicroservice.model.PaymentOrder;
import com.vivek.paymentMicroservice.payload.dto.BookingDTO;
import com.vivek.paymentMicroservice.payload.dto.UserDTO;
import com.vivek.paymentMicroservice.payload.response.PaymentLinkResponse;
import com.vivek.paymentMicroservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO booking,
            @RequestBody UserDTO user,
            @RequestParam PaymentMethod paymentMethod
    ) throws Exception, RazorpayException, StripeException {
        
        PaymentLinkResponse paymentLinkResponse = paymentService.createOrder(user, booking, paymentMethod);
        return ResponseEntity.ok(paymentLinkResponse);
    }


    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable Integer paymentOrderId
    ) throws Exception {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(paymentOrder);
    }


    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId
    ) throws Exception {

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        Boolean success = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId, paymentLinkId
        );
        return ResponseEntity.ok(success);
    }

}

