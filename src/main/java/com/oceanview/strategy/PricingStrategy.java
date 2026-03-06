package com.oceanview.strategy;

import java.sql.Date;

/**
 * Pricing Strategy Interface
 * 
 * DESIGN PATTERN: Strategy
 * - Defines a family of pricing algorithms
 * - Encapsulates each algorithm
 * - Makes them interchangeable at runtime
 * 
 * PRINCIPLE: Open/Closed Principle (OCP)
 * - New pricing strategies can be added without modifying existing code
 * 
 * PRINCIPLE: Interface Segregation Principle (ISP)
 * - Small, focused interface with single responsibility
 */
public interface PricingStrategy {
    
    /**
     * Calculate price for a stay
     * @param roomType The type of room
     * @param checkIn Check-in date
     * @param checkOut Check-out date
     * @return Calculated price
     */
    double calculatePrice(String roomType, Date checkIn, Date checkOut);
    
    /**
     * Get the strategy name
     * @return Strategy name
     */
    String getStrategyName();
}
