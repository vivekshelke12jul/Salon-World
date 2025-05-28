package com.vivek.notificationMicroservice.controller;

import com.vivek.notificationMicroservice.mapper.NotificationMapper;
import com.vivek.notificationMicroservice.model.Notification;
import com.vivek.notificationMicroservice.payload.dto.BookingDTO;
import com.vivek.notificationMicroservice.payload.dto.NotificationDTO;
import com.vivek.notificationMicroservice.service.NotificationService;
import com.vivek.notificationMicroservice.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications/salon-owner")
public class SalonNotificationController {

    @Autowired private NotificationService notificationService;
    @Autowired private NotificationMapper notificationMapper;
    @Autowired
    private BookingFeignClient bookingFeignClient;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsBySalonId(@PathVariable Integer salonId) {
        List<Notification> notifications = notificationService.getAllNotificationsBySalonId(salonId);
        List<NotificationDTO> notificationDTOS=notifications
                .stream()
                .map((notification)-> {
                    BookingDTO bookingDTO= bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
                    return notificationMapper.toDTO(notification,bookingDTO);
                }).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDTOS);
    }
}
