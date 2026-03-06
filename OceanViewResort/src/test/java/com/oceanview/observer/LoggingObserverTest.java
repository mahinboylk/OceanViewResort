package com.oceanview.observer;

import com.oceanview.model.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoggingObserver
 * Tests Observer pattern implementation
 */
public class LoggingObserverTest {
    
    private LoggingObserver loggingObserver;
    private ByteArrayOutputStream outputStream;
    private Handler logHandler;
    private Logger logger;
    
    @BeforeEach
    void setUp() {
        loggingObserver = new LoggingObserver();
        outputStream = new ByteArrayOutputStream();
        
        // Get the logger and add a custom handler to capture output
        logger = Logger.getLogger(LoggingObserver.class.getName());
        logHandler = new StreamHandler(outputStream, new java.util.logging.SimpleFormatter()) {
            @Override
            public void publish(java.util.logging.LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        logger.addHandler(logHandler);
    }
    
    // Test 1: Log reservation created event
    @Test
    void testOnReservationCreatedLogsEvent() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestName("John Doe");
        reservation.setRoomType("Standard");
        reservation.setCheckIn(Date.valueOf("2024-01-01"));
        reservation.setCheckOut(Date.valueOf("2024-01-03"));
        
        // Act
        loggingObserver.onReservationCreated(reservation);
        
        // Assert
        String logOutput = outputStream.toString();
        assertTrue(logOutput.contains("Reservation Created"), "Log should contain event type");
        assertTrue(logOutput.contains("John Doe"), "Log should contain guest name");
    }
    
    // Test 2: Log reservation cancelled event
    @Test
    void testOnReservationCancelledLogsEvent() {
        // Act
        loggingObserver.onReservationCancelled(100, "Guest request");
        
        // Assert
        String logOutput = outputStream.toString();
        assertTrue(logOutput.contains("Reservation Cancelled"), "Log should contain cancellation event");
        assertTrue(logOutput.contains("100"), "Log should contain reservation ID");
    }
    
    // Test 3: Log status changed event
    @Test
    void testOnReservationStatusChangedLogsEvent() {
        // Act
        loggingObserver.onReservationStatusChanged(100, "Active", "Cancelled");
        
        // Assert
        String logOutput = outputStream.toString();
        assertTrue(logOutput.contains("Status Changed"), "Log should contain status change event");
        assertTrue(logOutput.contains("Active"), "Log should contain old status");
        assertTrue(logOutput.contains("Cancelled"), "Log should contain new status");
    }
    
    // Test 4: Handle null reason gracefully
    @Test
    void testOnReservationCancelledWithNullReason() {
        // Act & Assert
        assertDoesNotThrow(() -> loggingObserver.onReservationCancelled(100, null), 
            "Should handle null reason without exception");
    }
    
    // Test 5: Multiple events logged
    @Test
    void testMultipleEvents() {
        // Act
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestName("Jane Doe");
        reservation.setRoomType("Deluxe");
        reservation.setCheckIn(Date.valueOf("2024-02-01"));
        reservation.setCheckOut(Date.valueOf("2024-02-05"));
        
        loggingObserver.onReservationCreated(reservation);
        loggingObserver.onReservationStatusChanged(1, "Active", "Completed");
        
        // Assert
        String logOutput = outputStream.toString();
        assertTrue(logOutput.contains("Jane Doe"));
        assertTrue(logOutput.contains("Completed"));
    }
}
