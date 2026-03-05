package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class SeasonalPricingStrategyTest_RED {
    
    @Test
    void testCalculatePriceForDeluxeRoom() {
        // Arrange: Setup pricing strategy and peak season dates
        SeasonalPricingStrategy strategy = new SeasonalPricingStrategy();
        Date checkIn = Date.valueOf(LocalDate.of(2024, 12, 15)); // Peak season (December)
        Date checkOut = Date.valueOf(LocalDate.of(2024, 12, 17)); // 2 nights
        
        // Act: Calculate price for Deluxe room during peak season
        double actualPrice = strategy.calculatePrice("Deluxe", checkIn, checkOut);
        
        // Assert: Verify the calculated price
        // Deluxe room: 35000 LKR per night x 2 nights x 1.5 peak multiplier = 105000 LKR
        // This assertion uses WRONG expected value to demonstrate RED phase
        double wrongExpectedPrice = 1.0; // Intentionally wrong for RED phase
        assertEquals(wrongExpectedPrice, actualPrice, 0.01, 
            "RED PHASE: Peak season Deluxe price should match WRONG expected value (" + 
            wrongExpectedPrice + ") to demonstrate TDD. Actual price is " + actualPrice);
    }
}
