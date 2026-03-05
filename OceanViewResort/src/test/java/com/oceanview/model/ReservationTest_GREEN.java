package com.oceanview.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class ReservationTest_GREEN {
    
    @Test
    void testGuestNameGetterSetter() {
        // Arrange: Create a reservation and set guest name
        Reservation reservation = new Reservation();
        reservation.setGuestName("John Doe");
        
        // Act & Assert: Verify the guest name is stored and retrieved correctly
        // This assertion uses CORRECT expected value to demonstrate GREEN phase
        assertEquals("John Doe", reservation.getGuestName(), 
            "GREEN PHASE: Guest name getter should return the value set by setter");
    }
}
