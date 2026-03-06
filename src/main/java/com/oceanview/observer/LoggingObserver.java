package com.oceanview.observer;

import com.oceanview.model.Reservation;
import java.util.logging.Logger;

/**
 * Logging Observer - Logs reservation events
 * 
 * DESIGN PATTERN: Observer (Concrete Observer)
 * - Reacts to reservation events by logging them
 * - Demonstrates loose coupling with subject
 * 
 * PRINCIPLE: Single Responsibility Principle (SRP)
 * - Only responsible for logging reservation events
 * 
 * PRINCIPLE: Liskov Substitution Principle (LSP)
 * - Can substitute ReservationObserver without breaking behavior
 */
public class LoggingObserver implements ReservationObserver {

    private static final Logger logger = Logger.getLogger(LoggingObserver.class.getName());

    @Override
    public void onReservationCreated(Reservation reservation) {
        logger.info(String.format(
            "[LOG] Reservation Created - ID: %d, Guest: %s, Room: %s, Dates: %s to %s",
            reservation.getReservationId(),
            reservation.getGuestName(),
            reservation.getRoomType(),
            reservation.getCheckIn(),
            reservation.getCheckOut()
        ));
    }

    @Override
    public void onReservationCancelled(int reservationId, String reason) {
        logger.info(String.format(
            "[LOG] Reservation Cancelled - ID: %d, Reason: %s",
            reservationId,
            reason != null ? reason : "Not specified"
        ));
    }

    @Override
    public void onReservationStatusChanged(int reservationId, String oldStatus, String newStatus) {
        logger.info(String.format(
            "[LOG] Reservation Status Changed - ID: %d, Status: %s -> %s",
            reservationId,
            oldStatus,
            newStatus
        ));
    }

    @Override
    public String getObserverName() {
        return "Logging Observer";
    }
}
