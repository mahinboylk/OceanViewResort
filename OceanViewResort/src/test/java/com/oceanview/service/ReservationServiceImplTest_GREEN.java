package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.Date;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class ReservationServiceImplTest_GREEN {
    
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
        
        // Assert: Verify reservation is created successfully
        assertTrue(actualResult, 
            "GREEN PHASE: Reservation for " + reservation.getGuestName() + 
            " should be created successfully when room is available. Actual result: " + actualResult);
    }
}
