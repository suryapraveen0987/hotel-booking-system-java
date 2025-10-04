package com.hotel.repository;

import com.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Finds a room by its room number. Useful for admin CRUD operations.
     */
    Optional<Room> findByRoomNumber(String roomNumber);

    /**
     * Finds all rooms of a specific type. Useful for customer search filters.
     */
    List<Room> findByRoomType(String roomType);

    /**
     * Finds all rooms that are currently marked as available.
     */
    List<Room> findByIsAvailableTrue();
}