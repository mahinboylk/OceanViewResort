-- Ocean View Resort - Database Migration Script
-- Run this script to add the 'status' column to the reservations table

-- Add status column if it doesn't exist
ALTER TABLE reservations 
ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'Active' AFTER check_out;

-- Update existing records to have 'Active' status if they have NULL status
UPDATE reservations 
SET status = 'Active' 
WHERE status IS NULL;

-- Create index on status for better query performance
CREATE INDEX IF NOT EXISTS idx_reservations_status ON reservations(status);

-- Create index on room_type and check_in/check_out for availability queries
CREATE INDEX IF NOT EXISTS idx_reservations_availability 
ON reservations(room_type, status, check_in, check_out);

-- Verify the changes
SELECT 'Migration completed successfully!' AS message;
SELECT COUNT(*) AS total_reservations, 
       SUM(CASE WHEN status = 'Active' THEN 1 ELSE 0 END) AS active_count,
       SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) AS cancelled_count
FROM reservations;
