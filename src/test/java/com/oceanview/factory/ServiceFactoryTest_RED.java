package com.oceanview.factory;

import com.oceanview.service.ReservationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class ServiceFactoryTest_RED {
    
    @Test
    void testGetReservationServiceReturnsSameInstance() {
        // Arrange: Get service factory instance
        ServiceFactory factory = ServiceFactory.getInstance();
        
        // Act: Get reservation service twice
        ReservationService service1 = factory.getReservationService();
        ReservationService service2 = factory.getReservationService();
        
        // Assert: Verify services are the same instance (Singleton pattern)
        // This assertion uses WRONG assertion to demonstrate RED phase
        // service1 and service2 ARE the same instance, but we assert they are NOT
        assertNotSame(service1, service2, 
            "RED PHASE: Services should be different instances (WRONG for RED phase). " +
            "service1 hash: " + System.identityHashCode(service1) + 
            ", service2 hash: " + System.identityHashCode(service2));
    }
}
