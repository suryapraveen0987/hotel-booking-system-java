package com.hotel.service;

import com.hotel.model.Location;
import com.hotel.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> findLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}