package com.oceanview.service;

import com.oceanview.model.Reservation;
import java.sql.Date;
import java.util.List;

/**
 * Service interface for Reservation business logic
 */
public interface ReservationService {
    
    /**
     * Create a new reservation with availability check
     * @param reservation The reservation to create
     * @return true if successful
     * @throws Exception if room not available or other error
     */
    boolean createReservation(Reservation reservation) throws Exception;
    
    /**
     * Cancel a reservation
     * @param reservationId The ID of the reservation to cancel
     * @return true if successful
     * @throws Exception if cancellation fails
     */
    boolean cancelReservation(int reservationId) throws Exception;
    
    /**
     * Get a reservation by ID
     * @param reservationId The ID to search for
     * @return Reservation object or null
     * @throws Exception if database error
     */
    Reservation getReservation(int reservationId) throws Exception;
    
    /**
     * Get all reservations
     * @return List of all reservations
     * @throws Exception if database error
     */
    List<Reservation> getAllReservations() throws Exception;
    
    /**
     * Check room availability
     * @param roomType The type of room
     * @param checkIn Check-in date
     * @param checkOut Check-out date
     * @return true if available
     * @throws Exception if database error
     */
    boolean checkAvailability(String roomType, Date checkIn, Date checkOut) throws Exception;
    
    /**
     * Calculate total amount for a reservation
     * @param roomType The type of room
     * @param checkIn Check-in date
     * @param checkOut Check-out date
     * @return The total amount
     */
    double calculateTotal(String roomType, Date checkIn, Date checkOut);
}
