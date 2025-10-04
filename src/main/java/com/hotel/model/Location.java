package com.hotel.model;

import jakarta.persistence.*;
import java.util.Set; // For the inverse relationship (optional, but good practice)

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Grand Hyatt NYC"

    private String city;

    private String address;

    // Inverse relationship: One Location has many Rooms
    // Use mappedBy to point to the 'location' field in the Room entity
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Room> rooms;

    // --- Constructors, Getters, and Setters ---
    public Location() {}
    
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    // ... (other getters/setters)
}