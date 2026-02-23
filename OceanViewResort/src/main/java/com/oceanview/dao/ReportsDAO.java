package com.oceanview.dao;

import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for Reports
 */
public interface ReportsDAO {
    
    /**
     * Get total revenue from active/completed reservations
     * @return Total revenue amount
     */
    double getTotalRevenue() throws Exception;
    
    /**
     * Get count of reservations by status
     * @param status The status to count
     * @return Count of reservations
     */
    int getCountByStatus(String status) throws Exception;
    
    /**
     * Get current occupancy count
     * @return Number of currently occupied rooms
     */
    int getCurrentOccupancy() throws Exception;
    
    /**
     * Get upcoming check-ins
     * @return List of upcoming check-in details
     */
    List<Map<String, Object>> getUpcomingCheckins() throws Exception;
    
    /**
     * Get revenue by room type
     * @return Map of room type to revenue
     */
    Map<String, Double> getRevenueByRoomType() throws Exception;
}
