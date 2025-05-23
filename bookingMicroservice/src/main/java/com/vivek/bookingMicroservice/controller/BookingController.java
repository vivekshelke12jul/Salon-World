package com.vivek.bookingMicroservice.controller;

import com.vivek.bookingMicroservice.domain.BookingStatus;
import com.vivek.bookingMicroservice.domain.PaymentMethod;
import com.vivek.bookingMicroservice.mapper.BookingMapper;
import com.vivek.bookingMicroservice.model.Booking;
import com.vivek.bookingMicroservice.model.SalonReport;
import com.vivek.bookingMicroservice.payload.dto.*;
import com.vivek.bookingMicroservice.payload.request.BookingRequest;
import com.vivek.bookingMicroservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody UserDTO user,
            @RequestParam Integer salonId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestBody BookingRequest bookingRequest
    ) throws Exception {

        SalonDTO salon = new SalonDTO();
        Set<ServiceOfferingDTO> serviceOfferingSet = new HashSet<>();
        Booking createdBooking = bookingService.createBooking(
                bookingRequest,
                user,
                salon,
                serviceOfferingSet
        );
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(@RequestBody UserDTO user) throws Exception {
        List<Booking> bookings = bookingService.getBookingsByCustomer(user.getId());
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(@RequestBody SalonDTO salon) throws Exception {
        SalonReport report = bookingService.getSalonReport(salon.getId());
        return ResponseEntity.ok(report);
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon (@RequestBody SalonDTO salon) throws Exception {
        List<Booking> bookings = bookingService.getBookingsBySalon(salon.getId());
        return ResponseEntity.ok(getBookingDTOs(bookings));
    }



    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {

        return bookings.stream()
                .map(booking -> {
                    Set<ServiceOfferingDTO> offeringDTOS = new HashSet<>();
                    SalonDTO salonDTO = new SalonDTO();
                    UserDTO user = new UserDTO();

                    return BookingMapper.toDTO(
                            booking,
                            offeringDTOS,
                            salonDTO,user
                    );
                })
                .collect(Collectors.toSet());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer bookingId) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);

        Set<ServiceOfferingDTO> offeringDTOS = new HashSet<>();
        BookingDTO bookingDTO=BookingMapper.toDTO(booking, offeringDTOS,null,null);
        return ResponseEntity.ok(bookingDTO);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Integer bookingId, @RequestParam BookingStatus status) throws Exception {

        Booking updatedBooking = bookingService.updateBookingStatus(bookingId, status);

        Set<ServiceOfferingDTO> offeringDTOS = new HashSet<>();
        SalonDTO salonDTO = new SalonDTO();
        UserDTO user = new UserDTO();

        BookingDTO bookingDTO=BookingMapper.toDTO(
                updatedBooking,
                offeringDTOS,
                salonDTO,
                user
        );
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookedSlotsDTO>> getBookedSlots (
            @PathVariable Integer salonId,
            @PathVariable LocalDate date
    ) throws Exception {

        List<Booking> bookings = bookingService.getBookingsByDate(date,salonId);

        List<BookedSlotsDTO> slotsDTOS = bookings.stream()
                .map(booking -> {
                    BookedSlotsDTO slotDto = new BookedSlotsDTO();
                    slotDto.setStartTime(booking.getStartTime());
                    slotDto.setEndTime(booking.getEndTime());

                    return slotDto;
                })
                .toList();

        return ResponseEntity.ok(slotsDTOS);
    }
}
