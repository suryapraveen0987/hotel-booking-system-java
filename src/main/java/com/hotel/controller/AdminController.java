package com.hotel.controller;

import com.hotel.model.Location; // NEW IMPORT
import com.hotel.model.Room;
import com.hotel.service.LocationService; // NEW IMPORT
import com.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
// All handlers in this controller are secured by the SecurityConfig to require ADMIN role
public class AdminController {

    private final RoomService roomService;
    private final LocationService locationService; // 1. INJECT NEW SERVICE

    @Autowired
    public AdminController(RoomService roomService, LocationService locationService) { // Update Constructor
        this.roomService = roomService;
        this.locationService = locationService; // Initialize new service
    }

    // --- 1. Manage Rooms (Default View & List) ---
    @GetMapping("/rooms")
    public String manageRooms(Model model) {
        // Always ensure the form object is present for adding new rooms
        if (!model.containsAttribute("newRoom")) {
             model.addAttribute("newRoom", new Room());
        }
        
        List<Room> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        
        // 2. Load all locations for the Room form dropdown
        model.addAttribute("locations", locationService.findAllLocations()); 
        
        return "admin/rooms-management"; 
    }
    
    // --- 2. Save/Update Room, Edit Room, Delete Room (Remain the same in structure) ---
    // ... (saveRoom, editRoom, deleteRoom methods are unchanged) ...

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute("newRoom") Room room, RedirectAttributes redirectAttributes) {
        try {
            roomService.saveOrUpdateRoom(room);
            redirectAttributes.addFlashAttribute("successMessage", 
                room.getRoomId() == null ? "Room added successfully!" : "Room updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving room: " + e.getMessage());
        }
        return "redirect:/admin/rooms";
    }
    
    @GetMapping("/rooms/edit/{id}")
    public String editRoom(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Room> roomOpt = roomService.findRoomById(id);

        if (roomOpt.isPresent()) {
            model.addAttribute("newRoom", roomOpt.get());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Room not found for editing.");
            return "redirect:/admin/rooms"; 
        }

        List<Room> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        
        // 2. Load all locations for the Room form dropdown
        model.addAttribute("locations", locationService.findAllLocations()); 
        
        return "admin/rooms-management";
    }

    @PostMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Room deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Cannot delete room. It may have existing bookings.");
        }
        return "redirect:/admin/rooms";
    }


    // --- 3. NEW: Location Management Handlers ---
    
    @GetMapping("/locations")
    public String manageLocations(Model model) {
        List<Location> locations = locationService.findAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("newLocation", new Location()); // For the Add New form
        return "admin/locations-management"; // Maps to the new template
    }

    @PostMapping("/locations/save")
    public String saveLocation(@ModelAttribute("newLocation") Location location, RedirectAttributes redirectAttributes) {
        try {
            locationService.saveLocation(location);
            redirectAttributes.addFlashAttribute("successMessage", 
                location.getLocationId() == null ? "Location added successfully!" : "Location updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving location: " + e.getMessage());
        }
        return "redirect:/admin/locations";
    }


    // --- User Management (Placeholder) ---
    @GetMapping("/users")
    public String manageUsers() {
        // Placeholder for user management view
        return "admin/users-management"; 
    }
}