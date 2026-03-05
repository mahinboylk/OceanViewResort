package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class StandardPricingStrategyTest_RED {
    
    @Test
    void testCalculatePriceForStandardRoom() {
        // Arrange: Setup pricing strategy and dates for 2 nights
        StandardPricingStrategy strategy = new StandardPricingStrategy();
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 3)); // 2 nights
        
        // Act: Calculate price for Standard room
        double actualPrice = strategy.calculatePrice("Standard", checkIn, checkOut);
        
        // Assert: Verify the calculated price
        // Standard room: 25000 LKR per night x 2 nights = 50000 LKR
        // This assertion uses WRONG expected value to demonstrate RED phase
        double wrongExpectedPrice = 99999.0; // Intentionally wrong for RED phase
        assertEquals(wrongExpectedPrice, actualPrice, 0.01, 
            "RED PHASE: Price should match WRONG expected value (" + wrongExpectedPrice + 
            ") to demonstrate TDD. Actual price is " + actualPrice);
    }
}
