package com.vivek.bookingMicroservice.repository;

import com.vivek.bookingMicroservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByCustomerId(Integer customerId);
    List<Booking> findBySalonId(Integer salonId);

}
