package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.Booking;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Finds all rooms that are available for the given date range.
     * @param checkInDate The requested check-in date.
     * @param checkOutDate The requested check-out date.
     * @return A list of available Room entities.
     */
    public List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        // Basic validation: Check-out must be after Check-in
        if (!checkOutDate.isAfter(checkInDate)) {
            return new ArrayList<>(); // Return empty list if dates are invalid
        }

        // 1. Get ALL rooms from the database
        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        // 2. Check each room for booking conflicts
        for (Room room : allRooms) {
            // Use the custom query from BookingRepository
            List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getRoomId(),
                checkInDate,
                checkOutDate
            );

            // If the list of conflicts is empty, the room is available
            if (conflicts.isEmpty()) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    /**
     * Admin functionality to retrieve all rooms.
     */
    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Admin functionality to save/update a room.
     */
    public Room saveOrUpdateRoom(Room room) {
        return roomRepository.save(room);
    }
    // Inside src/main/java/com/hotel/service/RoomService.java

// Add this method to RoomService
public Optional<Room> findRoomById(Long id) {
    return roomRepository.findById(id);
}

// Add this method to RoomService
public void deleteRoom(Long id) {
    roomRepository.deleteById(id);
}
}