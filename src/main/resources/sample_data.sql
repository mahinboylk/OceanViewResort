-- Sample Data for Ocean View Resort
-- Run this SQL after creating your database tables
-- All data is Sri Lanka related with local names, addresses, and phone numbers
-- Total Amount: Exactly Rs. 1,000,000

-- Use the database
-- USE ocean_view_db;

-- ============================================
-- Step 1: Add new columns to reservations table
-- Run these ALTER statements one by one
-- If column already exists, you'll get an error - that's OK, just continue
-- ============================================

-- Add email column (ignore error if already exists)
ALTER TABLE reservations ADD COLUMN email VARCHAR(100);

-- Add nic_passport column (ignore error if already exists)
ALTER TABLE reservations ADD COLUMN nic_passport VARCHAR(50);

-- ============================================
-- Step 2: Clear existing data (optional - uncomment if needed)
-- ============================================
-- DELETE FROM reservations;

-- ============================================
-- Step 3: Insert 10 sample reservations
-- Total: Rs. 1,000,000
-- ============================================

-- 1. Local Business Executive - Deluxe Room (2 nights)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Kasun Perera',
    'kasun.pera@gmail.com',
    '901234567V',
    '45, Galle Road, Colombo 03, Sri Lanka',
    '077-2345678',
    'Deluxe',
    '2026-03-01',
    '2026-03-03',
    70000.00,
    'Active'
);

-- 2. Family from Kandy - Suite Room (2 nights)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Nimal Fernando',
    'nimal.fernando@yahoo.com',
    '789012345V',
    '12, Temple Street, Kandy, Sri Lanka',
    '081-2234567',
    'Suite',
    '2026-03-05',
    '2026-03-07',
    100000.00,
    'Active'
);

-- 3. Couple from Mount Lavinia - Ocean View Room (2 nights)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Tharaka Seneviratne',
    'tharaka.s@hotmail.com',
    '923456789V',
    '78, Beach Road, Mount Lavinia, Sri Lanka',
    '071-9876543',
    'Ocean View',
    '2026-03-10',
    '2026-03-12',
    150000.00,
    'Active'
);

-- 4. Foreign Tourist - Presidential Suite (2 nights)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Sarah Johnson',
    'sarah.johnson@email.com',
    'P123456789',
    '123, Main Street, New York, USA',
    '+1-555-123-4567',
    'Presidential Suite',
    '2026-03-15',
    '2026-03-17',
    300000.00,
    'Active'
);

-- 5. Local Wedding Guest - Standard Room (1 night)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Amara Silva',
    'amara.silva@gmail.com',
    '887654321V',
    '34, Lake Drive, Nuwara Eliya, Sri Lanka',
    '076-3456789',
    'Standard',
    '2026-03-20',
    '2026-03-21',
    25000.00,
    'Active'
);

-- 6. Business Traveler from Colombo - Deluxe Room (1 night)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Roshan De Silva',
    'roshan.ds@outlook.com',
    '876543210V',
    '56, Union Place, Colombo 02, Sri Lanka',
    '011-2345678',
    'Deluxe',
    '2026-02-25',
    '2026-02-26',
    35000.00,
    'Completed'
);

-- 7. Family Vacation - Suite Room (2 nights)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Priya Wickramasinghe',
    'priya.w@gmail.com',
    '865432109V',
    '89, Matara Road, Galle, Sri Lanka',
    '066-2234567',
    'Suite',
    '2026-02-20',
    '2026-02-22',
    100000.00,
    'Completed'
);

-- 8. Cancelled Booking - Standard Room (1 night)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Chaminda Rathnayake',
    'chaminda.r@yahoo.com',
    '909876543V',
    '23, Kandy Road, Kadawatha, Sri Lanka',
    '072-4567890',
    'Standard',
    '2026-03-25',
    '2026-03-26',
    25000.00,
    'Cancelled'
);

-- 9. Honeymoon Couple - Ocean View Room (1 night)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Dilshan Madushanka',
    'dilshan.m@gmail.com',
    '919876543V',
    '45, Station Road, Negombo, Sri Lanka',
    '077-8765432',
    'Ocean View',
    '2026-04-01',
    '2026-04-02',
    75000.00,
    'Active'
);

-- 10. Corporate Executive - Presidential Suite (1 night)
INSERT INTO reservations (guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, total_amount, status)
VALUES (
    'Malini Jayawardena',
    'malini.j@company.lk',
    '701234567V',
    '100, York Street, Colombo 01, Sri Lanka',
    '011-5551234',
    'Presidential Suite',
    '2026-04-10',
    '2026-04-11',
    120000.00,
    'Active'
);

-- ============================================
-- Step 4: Verify the data
-- ============================================
SELECT * FROM reservations ORDER BY reservation_id DESC;

-- Summary statistics
SELECT 
    room_type,
    COUNT(*) as total_bookings,
    SUM(CASE WHEN status = 'Active' THEN 1 ELSE 0 END) as active_bookings,
    SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed_bookings,
    SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled_bookings,
    SUM(total_amount) as total_revenue
FROM reservations
GROUP BY room_type;

-- Grand Total Verification (Should be exactly 1,000,000)
SELECT 
    'GRAND TOTAL' as description,
    SUM(total_amount) as total_amount,
    COUNT(*) as total_reservations
FROM reservations;