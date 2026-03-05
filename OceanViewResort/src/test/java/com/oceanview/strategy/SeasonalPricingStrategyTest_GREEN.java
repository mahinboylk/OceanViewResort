package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class SeasonalPricingStrategyTest_GREEN {
    
    @Test
    void testCalculatePriceForDeluxeRoom() {
        // Arrange: Setup pricing strategy and peak season dates
        SeasonalPricingStrategy strategy = new SeasonalPricingStrategy();
        Date checkIn = Date.valueOf(LocalDate.of(2024, 12, 15)); // Peak season (December)
        Date checkOut = Date.valueOf(LocalDate.of(2024, 12, 17)); // 2 nights
        
        // Act: Calculate price for Deluxe room during peak season
        double actualPrice = strategy.calculatePrice("Deluxe", checkIn, checkOut);
        
        // Assert: Verify the calculated price is correct
        // Deluxe room: 35000 LKR per night x 2 nights x 1.5 peak multiplier = 105000 LKR
        double expectedPrice = 105000.0;
        assertEquals(expectedPrice, actualPrice, 0.01, 
            "GREEN PHASE: Deluxe room for 2 nights in peak season should cost " + expectedPrice + 
            " LKR (35000 x 2 nights x 1.5 peak multiplier). Actual: " + actualPrice);
    }
}
