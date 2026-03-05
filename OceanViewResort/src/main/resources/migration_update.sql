-- Ocean View Resort - Database Migration
-- Run this SQL to update existing tables with new columns

-- ============================================
-- 1. Update reservations table - Add new columns
-- ============================================
-- Add email column if not exists
SET @exist_email := (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'reservations' AND COLUMN_NAME = 'email');
SET @sql_email := IF(@exist_email = 0, 
    'ALTER TABLE reservations ADD COLUMN email VARCHAR(100) AFTER guest_name', 
    'SELECT "email column already exists"');
PREPARE stmt FROM @sql_email;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add nic_passport column if not exists
SET @exist_nic := (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'reservations' AND COLUMN_NAME = 'nic_passport');
SET @sql_nic := IF(@exist_nic = 0, 
    'ALTER TABLE reservations ADD COLUMN nic_passport VARCHAR(50) AFTER email', 
    'SELECT "nic_passport column already exists"');
PREPARE stmt FROM @sql_nic;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 2. Update users table - Add missing columns
-- ============================================
-- Add full_name column if not exists
SET @exist_fname := (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'full_name');
SET @sql_fname := IF(@exist_fname = 0, 
    'ALTER TABLE users ADD COLUMN full_name VARCHAR(100) AFTER password', 
    'SELECT "full_name column already exists"');
PREPARE stmt FROM @sql_fname;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add role column if not exists
SET @exist_role := (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'role');
SET @sql_role := IF(@exist_role = 0, 
    'ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT "staff" AFTER full_name', 
    'SELECT "role column already exists"');
PREPARE stmt FROM @sql_role;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add created_at column if not exists
SET @exist_created := (SELECT COUNT(*) FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'users' AND COLUMN_NAME = 'created_at');
SET @sql_created := IF(@exist_created = 0, 
    'ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP', 
    'SELECT "created_at column already exists"');
PREPARE stmt FROM @sql_created;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 3. Create room_types table if not exists
-- ============================================
CREATE TABLE IF NOT EXISTS room_types (
    room_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    base_price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    max_occupancy INT DEFAULT 2,
    amenities TEXT,
    INDEX idx_type_name (type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert room types with pricing (IGNORE duplicates)
INSERT IGNORE INTO room_types (type_name, base_price, description, max_occupancy) VALUES
('Standard', 25000.00, 'Comfortable room with essential amenities', 2),
('Deluxe', 35000.00, 'Spacious room with premium furnishings', 2),
('Suite', 55000.00, 'Luxury suite with separate living area', 4),
('Ocean View', 75000.00, 'Premium room with stunning ocean views', 2),
('Presidential Suite', 150000.00, 'Ultimate luxury with panoramic views and butler service', 4);

-- ============================================
-- 4. Create seasonal_pricing table if not exists
-- ============================================
CREATE TABLE IF NOT EXISTS seasonal_pricing (
    pricing_id INT AUTO_INCREMENT PRIMARY KEY,
    room_type VARCHAR(50) NOT NULL,
    season_name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    multiplier DECIMAL(3, 2) DEFAULT 1.00,
    INDEX idx_room_season (room_type, start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert seasonal pricing rules (IGNORE duplicates)
INSERT IGNORE INTO seasonal_pricing (room_type, season_name, start_date, end_date, multiplier) VALUES
('Standard', 'Peak Season', '2026-04-01', '2026-04-30', 1.25),
('Deluxe', 'Peak Season', '2026-04-01', '2026-04-30', 1.25),
('Suite', 'Peak Season', '2026-04-01', '2026-04-30', 1.30),
('Ocean View', 'Peak Season', '2026-04-01', '2026-04-30', 1.35),
('Presidential Suite', 'Peak Season', '2026-04-01', '2026-04-30', 1.40);

-- ============================================
-- 5. Update admin user with full_name if missing
-- ============================================
UPDATE users SET full_name = 'Administrator' WHERE username = 'admin' AND full_name IS NULL;

-- ============================================
-- 6. Verify the updates
-- ============================================
SELECT '--- Reservations Table Structure ---' as info;
DESCRIBE reservations;

SELECT '--- Users Table Structure ---' as info;
DESCRIBE users;

SELECT '--- Room Types ---' as info;
SELECT * FROM room_types;

SELECT '--- Seasonal Pricing ---' as info;
SELECT * FROM seasonal_pricing;