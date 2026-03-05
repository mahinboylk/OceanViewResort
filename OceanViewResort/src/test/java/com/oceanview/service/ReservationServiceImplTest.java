package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.strategy.PricingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.Date;

/**
 * Unit tests for ReservationServiceImpl
 * Tests business logic for reservation management
 */
public class ReservationServiceImplTest {
    
    private ReservationServiceImpl reservationService;
    private ReservationDAO reservationDAO;
    private PricingStrategy pricingStrategy;
    
    @BeforeEach
    void setUp() {
        reservationDAO = mock(ReservationDAO.class);
        pricingStrategy = mock(PricingStrategy.class);
        reservationService = new ReservationServiceImpl(reservationDAO);
        
        // Inject pricing strategy using reflection
        try {
            java.lang.reflect.Field pricingField = ReservationServiceImpl.class.getDeclaredField("pricingStrategy");
            pricingField.setAccessible(true);
            pricingField.set(reservationService, pricingStrategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Test 1: Create reservation successfully
    @Test
    void testCreateReservationSuccessfully() throws Exception {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRoomType("Deluxe");
        reservation.setGuestName("Jane Smith");
        Date checkIn = Date.valueOf("2024-01-01");
        Date checkOut = Date.valueOf("2024-01-03");
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        
        when(pricingStrategy.calculatePrice("Deluxe", checkIn, checkOut)).thenReturn(70000.0);
        when(reservationDAO.isRoomAvailable(anyString(), any(), any())).thenReturn(true);
        when(reservationDAO.insert(any(Reservation.class))).thenReturn(true);
        
        // Act
        boolean result = reservationService.createReservation(reservation);
        
        // Assert
        assertTrue(result);
        verify(reservationDAO).insert(reservation);
    }
    
    // Test 2: Create reservation when room not available
    @Test
    void testCreateReservationWhenRoomNotAvailable() throws Exception {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setRoomType("Standard");
        Date checkIn = Date.valueOf("2024-01-01");
        Date checkOut = Date.valueOf("2024-01-03");
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        
        when(reservationDAO.isRoomAvailable(anyString(), any(), any())).thenReturn(false);
        
        // Act
        boolean result = reservationService.createReservation(reservation);
        
        // Assert
        assertFalse(result);
        verify(reservationDAO, never()).insert(any());
    }
    
    // Test 3: Cancel reservation
    @Test
    void testCancelReservation() throws Exception {
        // Arrange
        when(reservationDAO.cancel(100)).thenReturn(true);
        
        // Act
        boolean result = reservationService.cancelReservation(100);
        
        // Assert
        assertTrue(result);
        verify(reservationDAO).cancel(100);
    }
    
    // Test 4: Check room availability
    @Test
    void testCheckAvailability() throws Exception {
        // Arrange
        Date checkIn = Date.valueOf("2024-01-01");
        Date checkOut = Date.valueOf("2024-01-03");
        when(reservationDAO.isRoomAvailable("Deluxe", checkIn, checkOut)).thenReturn(true);
        
        // Act
        boolean result = reservationService.checkAvailability("Deluxe", checkIn, checkOut);
        
        // Assert
        assertTrue(result);
    }
}
