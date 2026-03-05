package com.oceanview.observer;

import com.oceanview.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class LoggingObserverTest_GREEN {
    
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
        
        // Assert: Verify observer handles reservation creation without throwing
        assertDoesNotThrow(() -> observer.onReservationCreated(reservation),
            "GREEN PHASE: onReservationCreated should complete without throwing exception " +
            "for reservation " + reservation.getReservationId() + " (" + reservation.getGuestName() + ")");
    }
}
