package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.observer.*;
import com.oceanview.strategy.PricingStrategy;
import com.oceanview.strategy.StandardPricingStrategy;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation Service Implementation
 * 
 * DESIGN PATTERNS APPLIED:
 * 
 * 1. Strategy Pattern - Uses PricingStrategy for price calculation
 *    - Allows changing pricing algorithm at runtime
 *    - Supports different pricing strategies (standard, seasonal)
 * 
 * 2. Observer Pattern - Implements ReservationSubject
 *    - Notifies observers when reservations change
 *    - Supports multiple observers (logging, notifications)
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for reservation business logic
 * 
 * 2. Open/Closed Principle (OCP)
 *    - Open for extension (new pricing strategies, new observers)
 *    - Closed for modification (existing code doesn't change)
 * 
 * 3. Dependency Inversion Principle (DIP)
 *    - Depends on abstractions (ReservationDAO, PricingStrategy)
 *    - Not on concrete implementations
 */
public class ReservationServiceImpl implements ReservationService, ReservationSubject {

    private ReservationDAO reservationDAO;
    private PricingStrategy pricingStrategy;
    private List<ReservationObserver> observers;
    
    /**
     * Constructor with dependency injection
     * @param reservationDAO The DAO to use
     */
    public ReservationServiceImpl(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
        this.pricingStrategy = new StandardPricingStrategy(); // Default strategy
        this.observers = new ArrayList<>();
        
        // Attach default observers
        attachObserver(new LoggingObserver());
    }
    
    /**
     * Constructor with full dependency injection
     * @param reservationDAO The DAO to use
     * @param pricingStrategy The pricing strategy to use
     */
    public ReservationServiceImpl(ReservationDAO reservationDAO, PricingStrategy pricingStrategy) {
        this.reservationDAO = reservationDAO;
        this.pricingStrategy = pricingStrategy;
        this.observers = new ArrayList<>();
        
        // Attach default observers
        attachObserver(new LoggingObserver());
    }

    // ================== Observer Pattern Implementation ==================
    
    @Override
    public void attachObserver(ReservationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detachObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // Notification is handled by specific methods
    }
    
    /**
     * Notify observers of reservation creation
     */
    private void notifyReservationCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }
    
    /**
     * Notify observers of reservation cancellation
     */
    private void notifyReservationCancelled(int reservationId, String reason) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservationId, reason);
        }
    }
    
    /**
     * Notify observers of status change
     */
    private void notifyStatusChanged(int reservationId, String oldStatus, String newStatus) {
        for (ReservationObserver observer : observers) {
            observer.onReservationStatusChanged(reservationId, oldStatus, newStatus);
        }
    }

    // ================== Strategy Pattern - Set Pricing Strategy ==================
    
    /**
     * Set a different pricing strategy at runtime
     * @param strategy The pricing strategy to use
     */
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    // ================== Business Logic Methods ==================

    @Override
    public boolean createReservation(Reservation reservation) throws Exception {
        // Validate dates
        if (reservation.getCheckOut().before(reservation.getCheckIn())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Check room availability
        if (!reservationDAO.isRoomAvailable(reservation.getRoomType(), 
                                            reservation.getCheckIn(), 
                                            reservation.getCheckOut())) {
            throw new IllegalStateException("Room is not available for the selected dates");
        }
        
        // Set default status
        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus("Active");
        }
        
        // Calculate total using strategy pattern
        double total = pricingStrategy.calculatePrice(
            reservation.getRoomType(),
            reservation.getCheckIn(),
            reservation.getCheckOut()
        );
        reservation.setTotalAmount(total);
        
        // Insert reservation
        boolean success = reservationDAO.insert(reservation);
        
        // Notify observers
        if (success) {
            notifyReservationCreated(reservation);
        }
        
        return success;
    }

    @Override
    public boolean cancelReservation(int reservationId) throws Exception {
        // Get old status before cancellation
        Reservation reservation = reservationDAO.findById(reservationId);
        String oldStatus = reservation != null ? reservation.getStatus() : "Active";
        
        boolean cancelled = reservationDAO.cancel(reservationId);
        
        if (cancelled) {
            // Notify observers
            notifyReservationCancelled(reservationId, "User requested cancellation");
            notifyStatusChanged(reservationId, oldStatus, "Cancelled");
        }
        
        return cancelled;
    }

    @Override
    public Reservation getReservation(int reservationId) throws Exception {
        Reservation reservation = reservationDAO.findById(reservationId);
        // Total amount is now stored in database, no need to recalculate
        return reservation;
    }

    @Override
    public List<Reservation> getAllReservations() throws Exception {
        return reservationDAO.findAll();
    }

    @Override
    public boolean checkAvailability(String roomType, Date checkIn, Date checkOut) throws Exception {
        return reservationDAO.isRoomAvailable(roomType, checkIn, checkOut);
    }

    @Override
    public double calculateTotal(String roomType, Date checkIn, Date checkOut) {
        return pricingStrategy.calculatePrice(roomType, checkIn, checkOut);
    }
}
