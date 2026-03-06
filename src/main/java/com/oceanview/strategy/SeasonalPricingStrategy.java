package com.oceanview.strategy;

import java.sql.Date;
import java.util.Calendar;

/**
 * Seasonal Pricing Strategy
 *
 * DESIGN PATTERN: Strategy (Concrete Strategy)
 * - Implements seasonal pricing with peak/off-peak rates
 * - Demonstrates interchangeable pricing algorithms
 *
 * PRINCIPLE: Open/Closed Principle (OCP)
 * - New pricing behaviour added without modifying existing strategies
 *
 * PRINCIPLE: Liskov Substitution Principle (LSP)
 * - Substitutable for PricingStrategy interface
 */
public class SeasonalPricingStrategy implements PricingStrategy {

    // Base room rates per night in Sri Lankan Rupees — matching JSP display
    private static final double PRESIDENTIAL_SUITE_RATE = 150000.00;
    private static final double OCEAN_VIEW_RATE         =  75000.00;
    private static final double SUITE_RATE              =  55000.00;
    private static final double DELUXE_RATE             =  35000.00;
    private static final double STANDARD_RATE           =  25000.00;

    // Seasonal multipliers
    private static final double PEAK_SEASON_MULTIPLIER = 1.5;  // 50% premium
    private static final double OFF_PEAK_MULTIPLIER    = 0.8;  // 20% discount
    private static final double NORMAL_MULTIPLIER      = 1.0;

    @Override
    public double calculatePrice(String roomType, Date checkIn, Date checkOut) {
        long nights             = calculateNights(checkIn, checkOut);
        double baseRate         = getRoomRate(roomType);
        double seasonalMultiplier = getSeasonalMultiplier(checkIn);
        return nights * baseRate * seasonalMultiplier;
    }

    @Override
    public String getStrategyName() {
        return "Seasonal Pricing";
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

    /**
     * Get seasonal price multiplier based on check-in month.
     *
     * Peak season   (×1.5): December, January, July, August
     *   — Sri Lanka high season: Dec/Jan = southern coast holiday influx,
     *     Jul/Aug = northern coast peak
     *
     * Off-peak      (×0.8): May, June, September
     *   — Shoulder / monsoon months with lower demand
     *
     * Normal        (×1.0): all other months
     */
    private double getSeasonalMultiplier(Date date) {
        if (date == null) {
            return NORMAL_MULTIPLIER;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH); // 0-indexed: Jan=0, Dec=11

        // Peak: December (11), January (0), July (6), August (7)
        if (month == 11 || month == 0 || month == 6 || month == 7) {
            return PEAK_SEASON_MULTIPLIER;
        }

        // Off-peak: May (4), June (5), September (8)
        if (month == 4 || month == 5 || month == 8) {
            return OFF_PEAK_MULTIPLIER;
        }

        return NORMAL_MULTIPLIER;
    }
}