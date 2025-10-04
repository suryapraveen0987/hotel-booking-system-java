package com.hotel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(unique = true, nullable = false, length = 10)
    private String roomNumber;

    @Column(nullable = false, length = 50)
    private String roomType; // e.g., "Single", "Double", "Suite"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer capacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isAvailable = true;
    
    // --- NEW FIELD: Link to Location Entity ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false) // Foreign key column name
    private Location location;


    // --- Constructors ---
    public Room() {
    }

    // Updated constructor to include Location
    public Room(String roomNumber, String roomType, BigDecimal price, Integer capacity, String description, Location location) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
        this.location = location; // Initialize the new field
    }
    
    // --- Getters and Setters (Existing) ---
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    // --- Getters and Setters (NEW Location Field) ---
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}