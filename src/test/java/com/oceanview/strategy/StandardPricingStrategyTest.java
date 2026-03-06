package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Unit tests for StandardPricingStrategy
 * Tests pricing calculations for different room types
 */
public class StandardPricingStrategyTest {
    
    private StandardPricingStrategy pricingStrategy;
    
    @BeforeEach
    void setUp() {
        pricingStrategy = new StandardPricingStrategy();
    }
    
    // Test 1: Standard room pricing for 3 nights
    @Test
    void testCalculatePriceForStandardRoom() {
        // Standard room: 25000 per night
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 4)); // 3 nights
        double price = pricingStrategy.calculatePrice("Standard", checkIn, checkOut);
        assertEquals(75000.0, price, 0.01);
    }
    
    // Test 2: Deluxe room pricing for 2 nights
    @Test
    void testCalculatePriceForDeluxeRoom() {
        // Deluxe room: 35000 per night
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 3)); // 2 nights
        double price = pricingStrategy.calculatePrice("Deluxe", checkIn, checkOut);
        assertEquals(70000.0, price, 0.01);
    }
    
    // Test 3: Suite pricing for 2 nights
    @Test
    void testCalculatePriceForSuite() {
        // Suite: 55000 per night
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 3)); // 2 nights
        double price = pricingStrategy.calculatePrice("Suite", checkIn, checkOut);
        assertEquals(110000.0, price, 0.01);
    }
    
    // Test 4: Ocean View pricing for 1 night
    @Test
    void testCalculatePriceForOceanView() {
        // Ocean View: 75000 per night
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 2)); // 1 night
        double price = pricingStrategy.calculatePrice("Ocean View", checkIn, checkOut);
        assertEquals(75000.0, price, 0.01);
    }
    
    // Test 5: Presidential Suite pricing for 2 nights
    @Test
    void testCalculatePriceForPresidentialSuite() {
        // Presidential Suite: 150000 per night
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 3)); // 2 nights
        double price = pricingStrategy.calculatePrice("Presidential Suite", checkIn, checkOut);
        assertEquals(300000.0, price, 0.01);
    }
    
    // Test 6: Single night stay
    @Test
    void testCalculatePriceForSingleNight() {
        Date checkIn = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 1, 2)); // 1 night
        double price = pricingStrategy.calculatePrice("Standard", checkIn, checkOut);
        assertEquals(25000.0, price, 0.01);
    }
    
    // Test 7: Strategy name
    @Test
    void testGetStrategyName() {
        assertEquals("Standard Pricing", pricingStrategy.getStrategyName());
    }
}
