package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService; 

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
    }

    /**
     * Calculates the total price for a booking.
     */
    public BigDecimal calculateTotalPrice(Room room, Booking booking) {
        // Essential check: If room or price is null, prevent crash.
        if (room == null || room.getPrice() == null) {
            System.err.println("ERROR: Attempted to calculate price for a room with null price or room object.");
            return BigDecimal.ZERO; 
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        
        // Ensure check-out is strictly after check-in
        if (nights <= 0) {
            return BigDecimal.ZERO;
        }
        
        return room.getPrice().multiply(BigDecimal.valueOf(nights));
    }

    /**
     * Creates and saves a new booking after performing final checks.
     */
    @Transactional
    public Booking createBooking(Booking booking) {
        Room room = booking.getRoom();
        
        // CRITICAL FIX: Check for essential booking data integrity before proceeding.
        if (room == null || booking.getCheckInDate() == null || booking.getCheckOutDate() == null) {
            System.err.println("ERROR: Booking object is missing required Room or Date fields.");
            return null;
        }

        // 1. Re-check availability just before confirming (avoids race conditions)
        List<Room> availableRooms = roomService.findAvailableRooms(
            booking.getCheckInDate(), 
            booking.getCheckOutDate()
        );
        
        // Ensure the desired room ID is in the available list
        boolean isStillAvailable = availableRooms.stream()
                .anyMatch(r -> r.getRoomId() != null && r.getRoomId().equals(room.getRoomId()));

        if (!isStillAvailable) {
            return null; // Room is no longer available, fail the booking
        }
        
        // 2. Calculate the final price
        BigDecimal finalPrice = calculateTotalPrice(room, booking);
        
        // If price calculation returned zero due to error/invalid dates, abort
        if (finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        
        booking.setTotalPrice(finalPrice);

        // 3. Save the booking
        return bookingRepository.save(booking);
    }

    /**
     * Finds all bookings for a specific user.
     * FIX: Added @Transactional to keep the Hibernate session open
     * so Thymeleaf can access LAZY-loaded properties (like booking.room).
     */
    @Transactional(readOnly = true)
    public List<Booking> findUserBookings(User user) {
        if (user == null) {
            return List.of();
        }
        return bookingRepository.findByUser(user);
    }

    /**
     * Cancels a booking by updating its status.
     */
    public boolean cancelBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return true;
        }).orElse(false);
    }
}