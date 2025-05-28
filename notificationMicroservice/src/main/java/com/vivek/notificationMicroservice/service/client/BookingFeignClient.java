package com.vivek.notificationMicroservice.service.client;

import com.vivek.notificationMicroservice.payload.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("BOOKINGMICROSERVICE")
public interface BookingFeignClient {

    @GetMapping("/api/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer bookingId);
}
