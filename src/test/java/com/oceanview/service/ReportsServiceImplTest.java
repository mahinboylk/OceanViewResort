package com.oceanview.service;

import com.oceanview.dao.ReportsDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ReportsServiceImpl
 * Tests reporting and analytics functionality
 */
public class ReportsServiceImplTest {
    
    private ReportsServiceImpl reportsService;
    private ReportsDAO reportsDAO;
    
    @BeforeEach
    void setUp() {
        reportsDAO = mock(ReportsDAO.class);
        reportsService = new ReportsServiceImpl(reportsDAO);
    }
    
    // Test 1: Get total revenue
    @Test
    void testGetTotalRevenue() throws Exception {
        // Arrange
        when(reportsDAO.getTotalRevenue()).thenReturn(500000.0);
        
        // Act
        double revenue = reportsService.getTotalRevenue();
        
        // Assert
        assertEquals(500000.0, revenue, 0.01);
        verify(reportsDAO).getTotalRevenue();
    }
    
    // Test 2: Get active reservation count
    @Test
    void testGetActiveReservationCount() throws Exception {
        // Arrange
        when(reportsDAO.getCountByStatus("Active")).thenReturn(10);
        
        // Act
        int count = reportsService.getActiveReservationCount();
        
        // Assert
        assertEquals(10, count);
        verify(reportsDAO).getCountByStatus("Active");
    }
    
    // Test 3: Get cancelled reservation count
    @Test
    void testGetCancelledReservationCount() throws Exception {
        // Arrange
        when(reportsDAO.getCountByStatus("Cancelled")).thenReturn(2);
        
        // Act
        int count = reportsService.getCancelledReservationCount();
        
        // Assert
        assertEquals(2, count);
        verify(reportsDAO).getCountByStatus("Cancelled");
    }
    
    // Test 4: Get completed reservation count
    @Test
    void testGetCompletedReservationCount() throws Exception {
        // Arrange
        when(reportsDAO.getCountByStatus("Completed")).thenReturn(5);
        
        // Act
        int count = reportsService.getCompletedReservationCount();
        
        // Assert
        assertEquals(5, count);
        verify(reportsDAO).getCountByStatus("Completed");
    }
    
    // Test 5: Get current occupancy
    @Test
    void testGetCurrentOccupancy() throws Exception {
        // Arrange
        when(reportsDAO.getCurrentOccupancy()).thenReturn(15);
        
        // Act
        int occupancy = reportsService.getCurrentOccupancy();
        
        // Assert
        assertEquals(15, occupancy);
        verify(reportsDAO).getCurrentOccupancy();
    }
    
    // Test 6: Get occupancy rate
    @Test
    void testGetOccupancyRate() throws Exception {
        // Arrange
        when(reportsDAO.getCurrentOccupancy()).thenReturn(15);
        // TOTAL_ROOMS = 20, so rate = 15/20 * 100 = 75%
        
        // Act
        double rate = reportsService.getOccupancyRate();
        
        // Assert
        assertEquals(75.0, rate, 0.01);
    }
    
    // Test 7: Get zero revenue
    @Test
    void testGetTotalRevenueWhenZero() throws Exception {
        // Arrange
        when(reportsDAO.getTotalRevenue()).thenReturn(0.0);
        
        // Act
        double revenue = reportsService.getTotalRevenue();
        
        // Assert
        assertEquals(0.0, revenue, 0.01);
    }
    
    // Test 8: Revenue by room type
    @Test
    void testGetRevenueByRoomType() throws Exception {
        // Arrange
        Map<String, Double> revenueMap = new HashMap<>();
        revenueMap.put("Standard", 100000.0);
        revenueMap.put("Deluxe", 200000.0);
        revenueMap.put("Suite", 150000.0);
        when(reportsDAO.getRevenueByRoomType()).thenReturn(revenueMap);
        
        // Act
        Map<String, Double> result = reportsService.getRevenueByRoomType();
        
        // Assert
        assertEquals(3, result.size());
        assertEquals(100000.0, result.get("Standard"));
    }
}
