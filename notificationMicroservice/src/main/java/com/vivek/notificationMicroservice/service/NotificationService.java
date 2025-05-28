package com.vivek.notificationMicroservice.service;


import com.vivek.notificationMicroservice.mapper.NotificationMapper;
import com.vivek.notificationMicroservice.model.Notification;
import com.vivek.notificationMicroservice.payload.dto.BookingDTO;
import com.vivek.notificationMicroservice.payload.dto.NotificationDTO;
import com.vivek.notificationMicroservice.repository.NotificationRepository;
import com.vivek.notificationMicroservice.service.client.BookingFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private BookingFeignClient bookingFeignClient;



    public NotificationDTO createNotification(Notification notification) {
        Notification savedNotification= notificationRepository.save(notification);
        BookingDTO bookingDTO= bookingFeignClient
                .getBookingById(savedNotification.getBookingId()).getBody();

        NotificationDTO notificationDTO= notificationMapper.toDTO(
                savedNotification,
                bookingDTO
        );
        return notificationDTO;
    }

    public List<Notification> getAllNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getAllNotificationsBySalonId(Integer salonId) {
        return notificationRepository.findBySalonId(salonId);
    }

    public Notification markNotificationAsRead(Integer notificationId) throws Exception {
        return notificationRepository.findById(notificationId).map(notification -> {
            notification.setIsRead(true);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new Exception("Notification not found"));
    }

    public void deleteNotification(Integer notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        } else {
            throw new RuntimeException("Notification not found");
        }
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
