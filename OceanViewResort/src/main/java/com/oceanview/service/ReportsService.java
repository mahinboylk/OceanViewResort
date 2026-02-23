package com.oceanview.service;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Reports business logic
 */
public interface ReportsService {
    
    /**
     * Get total revenue
     * @return Total revenue amount
     */
    double getTotalRevenue() throws Exception;
    
    /**
     * Get count of active reservations
     * @return Count
     */
    int getActiveReservationCount() throws Exception;
    
    /**
     * Get count of cancelled reservations
     * @return Count
     */
    int getCancelledReservationCount() throws Exception;
    
    /**
     * Get count of completed reservations
     * @return Count
     */
    int getCompletedReservationCount() throws Exception;
    
    /**
     * Get current occupancy count
     * @return Number of occupied rooms
     */
    int getCurrentOccupancy() throws Exception;
    
    /**
     * Get occupancy rate percentage
     * @return Occupancy rate (0-100)
     */
    double getOccupancyRate() throws Exception;
    
    /**
     * Get upcoming check-ins
     * @return List of check-in details
     */
    List<Map<String, Object>> getUpcomingCheckins() throws Exception;
    
    /**
     * Get revenue by room type
     * @return Map of room type to revenue
     */
    Map<String, Double> getRevenueByRoomType() throws Exception;
}
