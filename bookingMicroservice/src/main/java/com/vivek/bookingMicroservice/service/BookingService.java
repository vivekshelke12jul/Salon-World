package com.vivek.bookingMicroservice.service;

import com.vivek.bookingMicroservice.domain.BookingStatus;
import com.vivek.bookingMicroservice.model.Booking;
import com.vivek.bookingMicroservice.model.PaymentOrder;
import com.vivek.bookingMicroservice.model.SalonReport;
import com.vivek.bookingMicroservice.payload.dto.SalonDTO;
import com.vivek.bookingMicroservice.payload.dto.ServiceOfferingDTO;
import com.vivek.bookingMicroservice.payload.dto.UserDTO;
import com.vivek.bookingMicroservice.payload.request.BookingRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {


    Booking createBooking(
            BookingRequest booking,
            UserDTO user,
            SalonDTO salon,
            Set<ServiceOfferingDTO> serviceOfferingSet
    ) throws Exception;


    List<Booking> getBookingsByCustomer(Integer customerId);


    List<Booking> getBookingsBySalon(Integer salonId);


    Booking getBookingById(Integer bookingId) throws Exception;

    Booking bookingSucess(PaymentOrder order) throws Exception;


    Booking updateBookingStatus(Integer bookingId, BookingStatus status) throws Exception;

    SalonReport getSalonReport(Integer salonId);

    List<Booking> getBookingsByDate(LocalDate date,Integer salonId);
}
