package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class StandardPricingStrategyTest_GREEN {
    
    @Test
    void testCalculatePriceForStandardRoom() {
        // Arrange: Setup pricing strategy and dates for 2 nights
        StandardPricingStrategy strategy = new StandardPricingStrategy();
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 3)); // 2 nights
        
        // Act: Calculate price for Standard room
        double actualPrice = strategy.calculatePrice("Standard", checkIn, checkOut);
        
        // Assert: Verify the calculated price is correct
        // Standard room: 25000 LKR per night x 2 nights = 50000 LKR
        double expectedPrice = 50000.0;
        assertEquals(expectedPrice, actualPrice, 0.01, 
            "GREEN PHASE: Standard room for 2 nights should cost " + expectedPrice + 
            " LKR (25000 x 2 nights). Actual: " + actualPrice);
    }
}
