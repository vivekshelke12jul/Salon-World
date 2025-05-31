
@FeignClient("BOOKING")
public interface BookingFeignClient {

    @GetMapping("/api/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId);
    
}
