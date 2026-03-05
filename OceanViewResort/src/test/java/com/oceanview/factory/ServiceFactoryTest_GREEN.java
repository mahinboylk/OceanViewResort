package com.oceanview.factory;

import com.oceanview.service.ReservationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class ServiceFactoryTest_GREEN {
    
    @Test
    void testGetReservationServiceReturnsSameInstance() {
        // Arrange: Get service factory instance
        ServiceFactory factory = ServiceFactory.getInstance();
        
        // Act: Get reservation service twice
        ReservationService service1 = factory.getReservationService();
        ReservationService service2 = factory.getReservationService();
        
        // Assert: Verify services are the same instance (Singleton pattern)
        assertSame(service1, service2, 
            "GREEN PHASE: ServiceFactory should return the same ReservationService instance " +
            "(Singleton pattern). Both services have hash: " + System.identityHashCode(service1));
    }
}
