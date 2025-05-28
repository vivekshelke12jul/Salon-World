package com.vivek.notificationMicroservice.repository;

import com.vivek.notificationMicroservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,
        Integer> {
    List<Notification> findByUserId(Integer userId);
    List<Notification> findBySalonId(Integer salonId);
}

