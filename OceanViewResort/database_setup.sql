-- =====================================================
-- Ocean View Resort - Database Setup Script
-- =====================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS ocean_view_db;
USE ocean_view_db;

-- =====================================================
-- Users Table - For authentication
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password) 
VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE username = username;

-- Insert a test user
INSERT INTO users (username, password) 
VALUES ('staff', 'staff123')
ON DUPLICATE KEY UPDATE username = username;

-- =====================================================
-- Reservations Table - For bookings
-- =====================================================
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    address TEXT,
    contact_number VARCHAR(20),
    room_type VARCHAR(50) NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Verify Tables
-- =====================================================
SELECT 'Users Table:' AS info;
SELECT * FROM users;

SELECT 'Database setup completed successfully!' AS status;