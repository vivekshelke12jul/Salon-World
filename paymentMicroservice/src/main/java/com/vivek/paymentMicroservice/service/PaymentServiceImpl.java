package com.vivek.paymentMicroservice.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vivek.paymentMicroservice.domain.PaymentMethod;
import com.vivek.paymentMicroservice.domain.PaymentOrderStatus;
import com.vivek.paymentMicroservice.messaging.BookingEventProducer;
import com.vivek.paymentMicroservice.messaging.NotificationEventProducer;
import com.vivek.paymentMicroservice.model.PaymentOrder;
import com.vivek.paymentMicroservice.payload.dto.BookingDTO;
import com.vivek.paymentMicroservice.payload.dto.UserDTO;
import com.vivek.paymentMicroservice.payload.response.PaymentLinkResponse;
import com.vivek.paymentMicroservice.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PaymentServiceImpl implements PaymentService {


    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private NotificationEventProducer notificationEventProducer;

    @Autowired
    private BookingEventProducer bookingEventProducer;

    @Override
    public PaymentLinkResponse createOrder(
            UserDTO user,
            BookingDTO booking,
            PaymentMethod paymentMethod
    ) throws RazorpayException, StripeException {

        Integer amount = booking.getTotalPrice();
        
        PaymentOrder order=new PaymentOrder();
        order.setUserId(user.getId());
        order.setAmount(amount);
        order.setBookingId(booking.getId());
        order.setSalonId(booking.getSalonId());
        order.setPaymentMethod(paymentMethod);
        PaymentOrder paymentOrder=paymentOrderRepository.save(order);

        PaymentLinkResponse res = new PaymentLinkResponse();
        
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink payment=createRazorpayPaymentLink(
                    user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId()
            );
            String paymentUrl=payment.get("short_url");
            String paymentUrlId=payment.get("id");

            res.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        }
        else{
            String paymentUrl=createStripePaymentLink(user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId());

            res.setPayment_link_url(paymentUrl);
        }
        return res;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Integer id) throws Exception {
        return paymentOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException( HttpStatus.NOT_FOUND,"payment order not found with id "+id)
                );
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception {
        return paymentOrderRepository.findByPaymentLinkId(paymentLinkId)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"payment order not found with payment link id "+paymentLinkId)
                );
    }

    @Override
    public Boolean ProceedPaymentOrder(
            PaymentOrder paymentOrder,
            String paymentId,
            String paymentLinkId
    ) throws RazorpayException {

        if(!paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            return false;
        }

        if(paymentOrder.getPaymentMethod().equals(PaymentMethod.STRIPE)){

            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }

        if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){

            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpay.payments.fetch(paymentId);

            Integer amount = payment.get("amount");
            String status = payment.get("status");

            if(!status.equals("captured")){
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }

            // notificaion
            notificationEventProducer.sentNotificationEvent(
                    paymentOrder.getBookingId(),
                    paymentOrder.getUserId(),
                    paymentOrder.getSalonId()
            );
            bookingEventProducer.sentBookingUpdateEvent(paymentOrder);

            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);

            return true;
        }

        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(
            UserDTO user,
            Integer Amount,
            Integer orderId
    ) throws RazorpayException {

        Integer amount = Amount * 100;

        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            // Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullName());

            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            // Set the reminder settings
            paymentLinkRequest.put("reminder_enable",true);

            // Set the callback URL and method
            paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            return razorpay.paymentLink.create(paymentLinkRequest);

        } catch (RazorpayException e) {

            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(
            UserDTO user,
            Integer amount,
            Integer orderId
    ) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100L)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}

