package com.oceanview.service;

import com.oceanview.dao.ReportsDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class ReportsServiceImplTest_GREEN {
    
    @Test
    void testGetTotalRevenue() throws Exception {
        // Arrange: Setup mock DAO
        ReportsDAO reportsDAO = mock(ReportsDAO.class);
        ReportsServiceImpl service = new ReportsServiceImpl(reportsDAO);
        
        double expectedRevenue = 500000.0;
        when(reportsDAO.getTotalRevenue()).thenReturn(expectedRevenue);
        
        // Act: Get total revenue
        double actualRevenue = service.getTotalRevenue();
        
        // Assert: Verify revenue matches expected value
        assertEquals(expectedRevenue, actualRevenue, 0.01, 
            "GREEN PHASE: Total revenue should be " + expectedRevenue + 
            " LKR. Actual revenue: " + actualRevenue);
    }
}
