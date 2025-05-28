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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired private  NotificationService notificationService;
    @Autowired private  NotificationMapper notificationMapper;
    @Autowired
    private  BookingFeignClient bookingFeignClient;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody Notification notification) {
        NotificationDTO createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getAllNotificationsByUserId(userId);

        List<NotificationDTO> notificationDTOS=notifications.stream().map((notification)-> {
            BookingDTO bookingDTO= bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
            return notificationMapper.toDTO(notification,bookingDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOS);
    }



    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Integer notificationId) throws Exception {
        Notification updatedNotification = notificationService.markNotificationAsRead(notificationId);
        BookingDTO bookingDTO= bookingFeignClient.getBookingById(updatedNotification.getBookingId()).getBody();

        NotificationDTO notificationDTO= notificationMapper.toDTO(
                updatedNotification,
                bookingDTO
        );

        return ResponseEntity.ok(notificationDTO);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
