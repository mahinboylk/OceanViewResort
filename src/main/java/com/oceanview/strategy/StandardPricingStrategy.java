package com.oceanview.strategy;

import java.sql.Date;

/**
 * Standard Pricing Strategy
 *
 * DESIGN PATTERN: Strategy (Concrete Strategy)
 * - Implements standard room pricing algorithm
 * - Can be swapped with other pricing strategies
 *
 * PRINCIPLE: Single Responsibility Principle (SRP)
 * - Only responsible for standard price calculation
 *
 * PRINCIPLE: Liskov Substitution Principle (LSP)
 * - Can substitute PricingStrategy interface without breaking behavior
 */
public class StandardPricingStrategy implements PricingStrategy {

    // Room rates per night in Sri Lankan Rupees — matching JSP display
    private static final double PRESIDENTIAL_SUITE_RATE = 150000.00;
    private static final double OCEAN_VIEW_RATE         =  75000.00;
    private static final double SUITE_RATE              =  55000.00;
    private static final double DELUXE_RATE             =  35000.00;
    private static final double STANDARD_RATE           =  25000.00;

    @Override
    public double calculatePrice(String roomType, Date checkIn, Date checkOut) {
        long nights = calculateNights(checkIn, checkOut);
        double rate = getRoomRate(roomType);
        return nights * rate;
    }

    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }

    /**
     * Calculate number of nights between check-in and check-out
     */
    private long calculateNights(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) {
            return 1;
        }
        long diff = checkOut.getTime() - checkIn.getTime();
        return Math.max(1, diff / (1000L * 60 * 60 * 24));
    }

    /**
     * Get nightly rate for the given room type
     */
    private double getRoomRate(String roomType) {
        if (roomType == null) {
            return STANDARD_RATE;
        }
        switch (roomType.toLowerCase().trim()) {
            case "presidential suite": return PRESIDENTIAL_SUITE_RATE;
            case "ocean view":         return OCEAN_VIEW_RATE;
            case "suite":              return SUITE_RATE;
            case "deluxe":             return DELUXE_RATE;
            default:                   return STANDARD_RATE;
        }
    }
}