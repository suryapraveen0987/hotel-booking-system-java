// main.js - Custom JavaScript for Hotel Booking System

document.addEventListener('DOMContentLoaded', () => {
    console.log('Hotel Booking System Frontend Loaded!');

    // --- Date Validation for Search Form ---
    const searchForm = document.getElementById('search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(event) {
            const checkIn = document.getElementById('checkInDate').value;
            const checkOut = document.getElementById('checkOutDate').value;

            if (checkIn && checkOut) {
                const inDate = new Date(checkIn);
                const outDate = new Date(checkOut);

                // Check if Check-out is strictly after Check-in
                if (outDate <= inDate) {
                    alert('Check-out date must be after Check-in date.');
                    event.preventDefault(); // Stop form submission
                }
            }
        });
    }
});