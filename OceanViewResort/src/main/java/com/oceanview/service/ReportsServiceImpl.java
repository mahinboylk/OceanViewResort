package com.oceanview.service;

import com.oceanview.dao.ReportsDAO;
import java.util.List;
import java.util.Map;

/**
 * Reports Service Implementation
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for reports business logic
 * 
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on ReportsDAO interface, not concrete implementation
 *    - DAO is injected through constructor
 * 
 * 3. Liskov Substitution Principle (LSP)
 *    - Can substitute ReportsService interface without breaking behavior
 */
public class ReportsServiceImpl implements ReportsService {

    private ReportsDAO reportsDAO;
    private static final int TOTAL_ROOMS = 20;
    
    /**
     * Constructor with dependency injection
     * @param reportsDAO The DAO to use for data access
     */
    public ReportsServiceImpl(ReportsDAO reportsDAO) {
        this.reportsDAO = reportsDAO;
    }

    @Override
    public double getTotalRevenue() throws Exception {
        return reportsDAO.getTotalRevenue();
    }

    @Override
    public int getActiveReservationCount() throws Exception {
        return reportsDAO.getCountByStatus("Active");
    }

    @Override
    public int getCancelledReservationCount() throws Exception {
        return reportsDAO.getCountByStatus("Cancelled");
    }

    @Override
    public int getCompletedReservationCount() throws Exception {
        return reportsDAO.getCountByStatus("Completed");
    }

    @Override
    public int getCurrentOccupancy() throws Exception {
        return reportsDAO.getCurrentOccupancy();
    }

    @Override
    public double getOccupancyRate() throws Exception {
        int occupancy = getCurrentOccupancy();
        return (double) occupancy / TOTAL_ROOMS * 100;
    }

    @Override
    public List<Map<String, Object>> getUpcomingCheckins() throws Exception {
        return reportsDAO.getUpcomingCheckins();
    }

    @Override
    public Map<String, Double> getRevenueByRoomType() throws Exception {
        return reportsDAO.getRevenueByRoomType();
    }
}
