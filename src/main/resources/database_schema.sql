-- Ocean View Resort - Database Schema
-- Run this SQL to create the complete database structure

-- Create reservations table with all columns
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    nic_passport VARCHAR(50),
    address TEXT,
    contact_number VARCHAR(20),
    room_type VARCHAR(50) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_amount DECIMAL(12, 2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for better query performance
    INDEX idx_room_type (room_type),
    INDEX idx_status (status),
    INDEX idx_check_in (check_in),
    INDEX idx_check_out (check_out),
    INDEX idx_guest_name (guest_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create users table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default admin user (password: admin123)
-- Note: In production, use proper password hashing
INSERT IGNORE INTO users (username, password, full_name, role)
VALUES ('admin', 'admin123', 'Administrator', 'admin');

-- Create room_types table for reference
CREATE TABLE IF NOT EXISTS room_types (
    room_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    base_price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    max_occupancy INT DEFAULT 2,
    amenities TEXT,
    
    INDEX idx_type_name (type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert room types with pricing
INSERT IGNORE INTO room_types (type_name, base_price, description, max_occupancy) VALUES
('Standard', 25000.00, 'Comfortable room with essential amenities', 2),
('Deluxe', 35000.00, 'Spacious room with premium furnishings', 2),
('Suite', 55000.00, 'Luxury suite with separate living area', 4),
('Ocean View', 75000.00, 'Premium room with stunning ocean views', 2),
('Presidential Suite', 150000.00, 'Ultimate luxury with panoramic views and butler service', 4);

-- Create seasonal_pricing table for dynamic pricing
CREATE TABLE IF NOT EXISTS seasonal_pricing (
    pricing_id INT AUTO_INCREMENT PRIMARY KEY,
    room_type VARCHAR(50) NOT NULL,
    season_name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    multiplier DECIMAL(3, 2) DEFAULT 1.00,
    
    INDEX idx_room_season (room_type, start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert seasonal pricing rules
INSERT IGNORE INTO seasonal_pricing (room_type, season_name, start_date, end_date, multiplier) VALUES
('Standard', 'Peak Season', '2026-04-01', '2026-04-30', 1.25),
('Deluxe', 'Peak Season', '2026-04-01', '2026-04-30', 1.25),
('Suite', 'Peak Season', '2026-04-01', '2026-04-30', 1.30),
('Ocean View', 'Peak Season', '2026-04-01', '2026-04-30', 1.35),
('Presidential Suite', 'Peak Season', '2026-04-01', '2026-04-30', 1.40),
('Standard', 'Off Season', '2026-05-01', '2026-09-30', 0.85),
('Deluxe', 'Off Season', '2026-05-01', '2026-09-30', 0.85),
('Suite', 'Off Season', '2026-05-01', '2026-09-30', 0.80),
('Ocean View', 'Off Season', '2026-05-01', '2026-09-30', 0.80),
('Presidential Suite', 'Off Season', '2026-05-01', '2026-09-30', 0.75);

-- Verify tables created
SHOW TABLES;

-- Show table structure
DESCRIBE reservations;
DESCRIBE users;
DESCRIBE room_types;
DESCRIBE seasonal_pricing;