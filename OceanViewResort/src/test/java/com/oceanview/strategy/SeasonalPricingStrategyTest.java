package com.oceanview.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Unit tests for SeasonalPricingStrategy
 * Tests Strategy pattern with seasonal pricing multipliers
 */
public class SeasonalPricingStrategyTest {
    
    private SeasonalPricingStrategy seasonalStrategy;
    
    @BeforeEach
    void setUp() {
        seasonalStrategy = new SeasonalPricingStrategy();
    }
    
    // Test 1: Peak season pricing (December - multiplier 1.5x)
    @Test
    void testCalculatePriceInPeakSeason() {
        // Peak season: December
        Date checkIn = Date.valueOf(LocalDate.of(2024, 12, 15));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 12, 17)); // 2 nights
        // Deluxe for 2 nights: 35000 * 2 = 70000, Peak: 70000 * 1.5 = 105000
        
        double actual = seasonalStrategy.calculatePrice("Deluxe", checkIn, checkOut);
        
        // Peak season: 70000 * 1.5 = 105000
        assertEquals(105000.0, actual, 0.01, "Peak season price should be 1.5x base price");
    }
    
    // Test 2: Off season pricing (May - multiplier 0.8x)
    @Test
    void testCalculatePriceInOffSeason() {
        // Off season: May
        Date checkIn = Date.valueOf(LocalDate.of(2024, 5, 15));
        Date checkOut = Date.valueOf(LocalDate.of(2024, 5, 18)); // 3 nights
        // Standard for 3 nights = 75000, Off season: 75000 * 0.8 = 60000
        
        double actual = seasonalStrategy.calculatePrice("Standard", checkIn, checkOut);
        
        // Off season: 75000 * 0.8 = 60000
        assertEquals(60000.0, actual, 0.01, "Off season price should be 0.8x base price");
    }
    
    // Test 3: Standard room seasonal pricing in normal season
    @Test
    void testStandardRoomSeasonalPrice() {
        Date checkIn = Date.valueOf(LocalDate.of(2024, 3, 15)); // March - normal season
        Date checkOut = Date.valueOf(LocalDate.of(2024, 3, 17)); // 2 nights
        
        double price = seasonalStrategy.calculatePrice("Standard", checkIn, checkOut);
        
        // Normal season: 25000 * 2 * 1.0 = 50000
        assertEquals(50000.0, price, 0.01, "Normal season should have no multiplier");
    }
    
    // Test 4: Presidential Suite seasonal pricing in peak season
    @Test
    void testPresidentialSuiteSeasonalPrice() {
        Date checkIn = Date.valueOf(LocalDate.of(2024, 12, 20)); // December - peak
        Date checkOut = Date.valueOf(LocalDate.of(2024, 12, 21)); // 1 night
        
        double price = seasonalStrategy.calculatePrice("Presidential Suite", checkIn, checkOut);
        
        // Peak season: 150000 * 1 * 1.5 = 225000
        assertEquals(225000.0, price, 0.01, "Presidential Suite peak season price");
    }
    
    // Test 5: Strategy name
    @Test
    void testGetStrategyName() {
        assertEquals("Seasonal Pricing", seasonalStrategy.getStrategyName());
    }
}
