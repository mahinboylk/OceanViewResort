package com.oceanview.observer;

import com.oceanview.model.Reservation;

/**
 * Observer Interface for Reservation Events
 * 
 * DESIGN PATTERN: Observer
 * - Defines a one-to-many dependency between objects
 * - When a reservation changes state, all observers are notified
 * - Supports loose coupling between subject and observers
 * 
 * PRINCIPLE: Interface Segregation Principle (ISP)
 * - Small, focused interface with single update method
 * 
 * PRINCIPLE: Dependency Inversion Principle (DIP)
 * - Subject depends on abstractions (Observer interface)
 */
public interface ReservationObserver {
    
    /**
     * Called when a reservation is created
     * @param reservation The created reservation
     */
    void onReservationCreated(Reservation reservation);
    
    /**
     * Called when a reservation is cancelled
     * @param reservationId The cancelled reservation ID
     * @param reason Optional reason for cancellation
     */
    void onReservationCancelled(int reservationId, String reason);
    
    /**
     * Called when a reservation status changes
     * @param reservationId The reservation ID
     * @param oldStatus Previous status
     * @param newStatus New status
     */
    void onReservationStatusChanged(int reservationId, String oldStatus, String newStatus);
    
    /**
     * Get observer name for logging
     * @return Observer name
     */
    String getObserverName();
}
