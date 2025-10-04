package com.hotel.repository;

import com.hotel.model.Booking;
import com.hotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * FIX: Uses JOIN FETCH to eagerly load the associated Room and User objects.
     * This prevents the LazyInitializationException crash when Thymeleaf accesses room details.
     */
    @Query("SELECT b FROM Booking b JOIN FETCH b.room JOIN FETCH b.user WHERE b.user = :user ORDER BY b.checkInDate DESC")
    List<Booking> findByUser(User user);

    /**
     * CUSTOM QUERY: Finds all bookings that conflict with the requested date range 
     * for a given room. This is CRITICAL for checking room availability.
     */
    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND " +
           "b.bookingStatus <> 'CANCELLED' AND " + // Only consider active bookings
           "(:checkInDate < b.checkOutDate AND :checkOutDate > b.checkInDate)")
    List<Booking> findConflictingBookings(
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    );
}