package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.Date;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class ReservationServiceImplTest_RED {
    
    @Test
    void testCreateReservationSuccessfully() throws Exception {
        // Arrange: Setup mock DAO and reservation
        ReservationDAO reservationDAO = mock(ReservationDAO.class);
        ReservationServiceImpl service = new ReservationServiceImpl(reservationDAO);
        
        Reservation reservation = new Reservation();
        reservation.setGuestName("John Doe");
        reservation.setRoomType("Standard");
        reservation.setCheckIn(Date.valueOf("2024-01-01"));
        reservation.setCheckOut(Date.valueOf("2024-01-03"));
        
        when(reservationDAO.isRoomAvailable(anyString(), any(), any())).thenReturn(true);
        when(reservationDAO.insert(any(Reservation.class))).thenReturn(true);
        
        // Act: Attempt to create reservation
        boolean actualResult = service.createReservation(reservation);
        
        // Assert: Verify reservation creation result
        // This assertion uses WRONG expected value to demonstrate RED phase
        // Actual result is true (successful creation), but we expect false
        assertFalse(actualResult, 
            "RED PHASE: Reservation creation should return WRONG value (false) to demonstrate TDD. " +
            "Actual result is " + actualResult + " because room is available and insert succeeded");
    }
}
