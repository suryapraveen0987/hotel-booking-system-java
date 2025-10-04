-- =======================================
-- SQL SCRIPT FOR HOTEL BOOKING SYSTEM
-- =======================================

-- 1. DATABASE CREATION (Optional, good practice to include)
-- You may keep this commented out if you rely on users creating the DB first.
-- CREATE DATABASE IF NOT EXISTS hotel_booking_system;
-- USE hotel_booking_system;

-- 2. TABLE DEFINITIONS (Mandatory)
-- These commands manually ensure the tables exist with the correct structure.
-- NOTE: Spring Boot with 'ddl-auto=update' usually does this, but these provide clarity.

-- Users Table (Includes the necessary VARCHAR(255) for BCrypt hashes)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER'
);

-- Locations Table (New table)
CREATE TABLE IF NOT EXISTS locations (
    location_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    city VARCHAR(255),
    address VARCHAR(255)
);

-- Rooms Table (Modified to include the location_id Foreign Key)
CREATE TABLE IF NOT EXISTS rooms (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_id BIGINT NOT NULL, 
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    
    -- Compound Unique Key: Room number must be unique WITHIN a location
    CONSTRAINT uc_room UNIQUE (location_id, room_number),
    
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    booking_status ENUM('CONFIRMED', 'PENDING', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
    
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id),
    CONSTRAINT chk_dates CHECK (check_out_date > check_in_date)
);

-- 3. INITIAL DATA INSERTION (Data to make the system runnable)

-- Insert Admin User (Requires a BCrypt hashed password, e.g., 'adminpass')
-- NOTE: Replace the hash with a real BCrypt hash for a common test password!
INSERT INTO users (username, password, email, role) 
VALUES ('admin', '$2a$10$fW3E.1J7yN7Yg/zL/4XWw.vQ.G.M.Q.Z.K.P.S.T.U.V.Y.Z.0', 'admin@hotel.com', 'ADMIN');

-- Insert Sample Locations (Necessary before inserting rooms)
INSERT INTO locations (name, city, address) 
VALUES 
('Central Business District Tower', 'New York', '123 Wall St'),
('Coastal Resort & Spa', 'Miami', '456 Ocean Dr');

-- Insert Sample Rooms
-- NOTE: location_id (1 or 2) must match the IDs created above.
INSERT INTO rooms (location_id, room_number, room_type, price, capacity, description, is_available) 
VALUES 
(1, '101', 'Standard Double', 120.00, 2, 'City view, comfortable queen bed.', TRUE),
(1, '205', 'Deluxe Suite', 250.00, 4, 'High floor, spacious suite.', TRUE),
(2, 'A1', 'Beachfront King', 350.00, 2, 'Ocean view with balcony.', TRUE);