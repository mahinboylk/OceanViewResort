package com.oceanview.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class ReservationTest_RED {
    
    @Test
    void testGuestNameGetterSetter() {
        // Arrange: Create a reservation and set guest name
        Reservation reservation = new Reservation();
        reservation.setGuestName("John Doe");
        
        // Act & Assert: Verify the guest name is stored correctly
        // This assertion uses WRONG expected value to demonstrate RED phase
        // Expected: "Jane Doe" (WRONG for RED phase)
        // Actual:   "John Doe" (what was set)
        assertEquals("Jane Doe", reservation.getGuestName(), 
            "RED PHASE: Guest name should match the WRONG expected value to demonstrate TDD");
    }
}
