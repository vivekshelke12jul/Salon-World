package com.vivek.paymentMicroservice.messaging;

import com.vivek.paymentMicroservice.model.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sentBookingUpdateEvent(PaymentOrder paymentOrder){
        rabbitTemplate.convertAndSend("booking-queue", paymentOrder);
    }
}

