package com.vivek.bookingMicroservice.service;

import com.vivek.bookingMicroservice.domain.BookingStatus;
import com.vivek.bookingMicroservice.model.Booking;
import com.vivek.bookingMicroservice.model.PaymentOrder;
import com.vivek.bookingMicroservice.model.SalonReport;
import com.vivek.bookingMicroservice.payload.dto.SalonDTO;
import com.vivek.bookingMicroservice.payload.dto.ServiceOfferingDTO;
import com.vivek.bookingMicroservice.payload.dto.UserDTO;
import com.vivek.bookingMicroservice.payload.request.BookingRequest;
import com.vivek.bookingMicroservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(
            BookingRequest booking,
            UserDTO user,
            SalonDTO salon,
            Set<ServiceOfferingDTO> serviceOfferingSet
    ) throws Exception {

        int totalDuration = serviceOfferingSet.stream()
                .mapToInt(ServiceOfferingDTO::getDuration)
                .sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable = isTimeSlotAvailable(salon,bookingStartTime,bookingEndTime);
        if(!isSlotAvailable){
            throw new Exception("Slot is not available");
        }

        int totalPrice = serviceOfferingSet.stream()
                .mapToInt(ServiceOfferingDTO::getPrice)
                .sum();

        Set<Integer> idList = serviceOfferingSet.stream()
                .map(ServiceOfferingDTO::getId)
                .collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setServiceIds(idList);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    public Boolean isTimeSlotAvailable(
            SalonDTO salon,
            LocalDateTime bookingStartTime,
            LocalDateTime bookingEndTime
    ) throws Exception {
        List<Booking> existingBookings = getBookingsBySalon(salon.getId());

        LocalDateTime salonOpenTime = salon.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salon.getCloseTime().atDate(bookingStartTime.toLocalDate());

        if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time must be within salon's open hours.");
        }

        for (Booking existingBooking : existingBookings) {
            LocalDateTime existingStartTime = existingBooking.getStartTime();
            LocalDateTime existingEndTime = existingBooking.getEndTime();

            // Check if new booking's time overlaps with any existing booking
            if ((bookingStartTime.isBefore(existingEndTime)
                    && bookingEndTime.isAfter(existingStartTime)) ||
                    bookingStartTime.isEqual(existingStartTime) || bookingEndTime.isEqual(existingEndTime)) {
                throw new Exception("slot not available, choose different time.");
            }
        }
        return true;
    }

    @Override
    public List<Booking> getBookingsByCustomer(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Integer salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Integer bookingId) throws Exception {
        return bookingRepository.findById(bookingId)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"booking not found")
                );
    }

    @Override
    public Booking bookingSucess(PaymentOrder order) throws Exception {
        Booking existingBooking = getBookingById(order.getBookingId());
        existingBooking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public Booking updateBookingStatus(Integer bookingId, BookingStatus status) throws Exception {
        Booking existingBooking = getBookingById(bookingId);
        existingBooking.setStatus(status);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public SalonReport getSalonReport(Integer salonId) {

        List<Booking> bookings = getBookingsBySalon(salonId);

        SalonReport report = new SalonReport();

        // Total Earnings: Sum of totalPrice for all bookings
        Double totalEarnings = bookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        // Total Bookings: Count of all bookings
        Integer totalBookings = bookings.size();

        // Cancelled Bookings: Filter bookings with status CANCELLED
        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus().toString().equalsIgnoreCase("CANCELLED"))
                .toList();

        // Refunds: Calculate based on cancelled bookings (same totalPrice as refunded)
        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();


        report.setTotalEarnings(totalEarnings);
        report.setTotalBookings(totalBookings);
        report.setCancelledBookings(cancelledBookings.size());
        report.setTotalRefund(totalRefund);

        return report;
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date,Integer salonId) {
        List<Booking> allBookings = bookingRepository.findBySalonId(salonId);

        if (date == null) {
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(), date) ||
                        isSameDate(booking.getEndTime(), date))
                .collect(Collectors.toList());
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }
}
