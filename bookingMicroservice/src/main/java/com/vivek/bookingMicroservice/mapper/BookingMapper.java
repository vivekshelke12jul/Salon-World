package com.vivek.bookingMicroservice.mapper;

import com.vivek.bookingMicroservice.model.Booking;
import com.vivek.bookingMicroservice.payload.dto.BookingDTO;
import com.vivek.bookingMicroservice.payload.dto.SalonDTO;
import com.vivek.bookingMicroservice.payload.dto.ServiceOfferingDTO;
import com.vivek.bookingMicroservice.payload.dto.UserDTO;

import java.util.Set;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking, Set<ServiceOfferingDTO> serviceOfferingDTOS, SalonDTO salonDTO, UserDTO userDTO) {
        if (booking == null) {
            return null;
        }

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setServicesIds(booking.getServiceIds());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setServices(serviceOfferingDTOS);
        bookingDTO.setCustomer(userDTO);
        bookingDTO.setSalon(salonDTO);

        return bookingDTO;
    }

    public static Booking toEntity(BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setSalonId(bookingDTO.getSalonId());
        booking.setCustomerId(bookingDTO.getCustomerId());
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());
        booking.setServiceIds(bookingDTO.getServicesIds());
        booking.setStatus(bookingDTO.getStatus());
        booking.setTotalPrice(bookingDTO.getTotalPrice());

        return booking;
    }
}