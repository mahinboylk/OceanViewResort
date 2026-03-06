package com.oceanview.observer;

import com.oceanview.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class LoggingObserverTest_RED {
    
    @Test
    void testOnReservationCreatedLogsEvent() {
        // Arrange: Create observer and reservation
        LoggingObserver observer = new LoggingObserver();
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestName("John Doe");
        reservation.setRoomType("Standard");
        reservation.setCheckIn(Date.valueOf("2024-01-01"));
        reservation.setCheckOut(Date.valueOf("2024-01-03"));
        
        // Assert: Verify observer handles reservation creation
        // This assertion uses WRONG expectation to demonstrate RED phase
        // Method does NOT throw exception, but we expect it to throw
        assertThrows(Exception.class, () -> observer.onReservationCreated(reservation),
            "RED PHASE: onReservationCreated should throw exception (WRONG for RED phase). " +
            "Method actually completes successfully without throwing");
    }
}
