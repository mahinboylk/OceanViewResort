package com.oceanview.service;

import com.oceanview.dao.ReportsDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class ReportsServiceImplTest_RED {
    
    @Test
    void testGetTotalRevenue() throws Exception {
        // Arrange: Setup mock DAO
        ReportsDAO reportsDAO = mock(ReportsDAO.class);
        ReportsServiceImpl service = new ReportsServiceImpl(reportsDAO);
        
        double expectedRevenue = 500000.0;
        when(reportsDAO.getTotalRevenue()).thenReturn(expectedRevenue);
        
        // Act: Get total revenue
        double actualRevenue = service.getTotalRevenue();
        
        // Assert: Verify revenue calculation
        // This assertion uses WRONG expected value to demonstrate RED phase
        double wrongExpectedRevenue = 999999.0; // Intentionally wrong for RED phase
        assertEquals(wrongExpectedRevenue, actualRevenue, 0.01, 
            "RED PHASE: Revenue should match WRONG expected value (" + wrongExpectedRevenue + 
            ") to demonstrate TDD. Actual revenue is " + actualRevenue);
    }
}
