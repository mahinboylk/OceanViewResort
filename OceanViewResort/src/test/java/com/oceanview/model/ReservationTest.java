package com.oceanview.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Reservation model class
 * Following Red-Green-Refactor TDD approach
 */
public class ReservationTest {
    
    private Reservation reservation;
    
    @BeforeEach
    void setUp() {
        reservation = new Reservation();
    }
    
    // Test 1: RED - Write failing test first
    @Test
    void testGuestNameGetterSetter() {
        // GREEN - Make it pass
        reservation.setGuestName("John Doe");
        assertEquals("John Doe", reservation.getGuestName());
    }
    
    // Test 2: Reservation ID
    @Test
    void testReservationIdGetterSetter() {
        reservation.setReservationId(100);
        assertEquals(100, reservation.getReservationId());
    }
    
    // Test 3: Total Amount Calculation
    @Test
    void testTotalAmountCalculation() {
        reservation.setTotalAmount(150000.00);
        assertEquals(150000.00, reservation.getTotalAmount(), 0.01);
    }
    
    // Test 4: Email getter/setter
    @Test
    void testEmailGetterSetter() {
        reservation.setEmail("john@example.com");
        assertEquals("john@example.com", reservation.getEmail());
    }
    
    // Test 5: Room Type
    @Test
    void testRoomTypeGetterSetter() {
        reservation.setRoomType("Deluxe");
        assertEquals("Deluxe", reservation.getRoomType());
    }
    
    // Test 6: Status
    @Test
    void testStatusGetterSetter() {
        reservation.setStatus("Active");
        assertEquals("Active", reservation.getStatus());
    }
    
    // Test 7: Contact number
    @Test
    void testContactNumberGetterSetter() {
        reservation.setContactNumber("0771234567");
        assertEquals("0771234567", reservation.getContactNumber());
    }
    
    // Test 8: NIC/Passport
    @Test
    void testNicPassportGetterSetter() {
        reservation.setNicPassport("901234567V");
        assertEquals("901234567V", reservation.getNicPassport());
    }
}
