package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.User;
import com.hotel.service.BookingService;
import com.hotel.service.RoomService;
import com.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public CustomerController(RoomService roomService, BookingService bookingService, UserService userService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.userService = userService;
    }
    
    // --- Home/Search Handler (Omitted for brevity, unchanged) ---
    @GetMapping({"/", "/search"})
    public String showSearchPage(Model model) {
        // ... (unchanged)
        model.addAttribute("checkInDate", LocalDate.now());
        model.addAttribute("checkOutDate", LocalDate.now().plusDays(1));
        
        if (!model.containsAttribute("availableRooms")) {
             model.addAttribute("availableRooms", List.of());
        }
        return "search"; 
    }

    @GetMapping("/rooms/available")
    public String findAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            Model model) {
        
        List<Room> availableRooms = roomService.findAvailableRooms(checkInDate, checkOutDate);
        
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        model.addAttribute("availableRooms", availableRooms);
        
        return "search"; 
    }

    // --- Booking Handlers (Omitted for brevity, unchanged) ---
    @PostMapping("/booking/create/{roomId}")
    public String createBooking(
            @PathVariable Long roomId,
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) { 
        
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());
        Optional<Room> roomOpt = roomService.findRoomById(roomId); 
        
        if (userOpt.isEmpty() || roomOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User session expired or Room not found.");
            return "redirect:/search";
        }
        
        Room room = roomOpt.get();
        User user = userOpt.get();

        Booking newBooking = new Booking(user, room, checkInDate, checkOutDate, null);
        Booking savedBooking = bookingService.createBooking(newBooking);

        if (savedBooking != null) {
            redirectAttributes.addFlashAttribute("booking", savedBooking);
            return "redirect:/booking/confirm";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Room is no longer available. Please search again.");
            return "redirect:/search";
        }
    }
    
    @GetMapping("/booking/confirm")
    public String showBookingConfirmation(@ModelAttribute("booking") Booking booking, Model model) {
        if (booking != null && booking.getBookingId() != null) {
            model.addAttribute("booking", booking);
            return "booking-confirm"; 
        }
        return "redirect:/search";
    }

    // --- My Bookings Handler FIX ---
    @GetMapping("/mybookings")
    public String viewMyBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<User> userOpt = userService.findByUsername(userDetails.getUsername());

        if (userOpt.isPresent()) {
            List<Booking> bookings = bookingService.findUserBookings(userOpt.get());
            model.addAttribute("bookings", bookings);
            
            // FIX: Add the current date to the model for use in Thymeleaf's cancellation logic
            model.addAttribute("currentDate", LocalDate.now()); 
            
            return "my-bookings"; // Maps to my-bookings.html
        }
        
        return "redirect:/login"; 
    }

    @PostMapping("/booking/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        if(bookingService.cancelBooking(bookingId)) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel booking. It may not exist.");
        }
        return "redirect:/mybookings";
    }
}