package com.oceanview.dao;

import com.oceanview.model.Reservation;
import java.sql.Date;
import java.util.List;

/**
 * Data Access Object interface for Reservation operations
 */
public interface ReservationDAO {
    
    /**
     * Insert a new reservation into the database
     * @param reservation The reservation to insert
     * @return true if successful, false otherwise
     */
    boolean insert(Reservation reservation) throws Exception;
    
    /**
     * Update reservation status to cancelled
     * @param reservationId The ID of the reservation to cancel
     * @return true if successful, false otherwise
     */
    boolean cancel(int reservationId) throws Exception;
    
    /**
     * Find a reservation by ID
     * @param reservationId The ID to search for
     * @return Reservation object or null if not found
     */
    Reservation findById(int reservationId) throws Exception;
    
    /**
     * Get all reservations
     * @return List of all reservations
     */
    List<Reservation> findAll() throws Exception;
    
    /**
     * Check if a room is available for the given dates
     * @param roomType The type of room
     * @param checkIn Check-in date
     * @param checkOut Check-out date
     * @return true if available, false if booked
     */
    boolean isRoomAvailable(String roomType, Date checkIn, Date checkOut) throws Exception;
}
