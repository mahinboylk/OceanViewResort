package com.oceanview.factory;

import com.oceanview.service.ReservationService;
import com.oceanview.service.ReportsService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ServiceFactory
 * Tests Singleton pattern and factory method implementation
 */
public class ServiceFactoryTest {
    
    // Test 1: Singleton - Same instance returned
    @Test
    void testGetReservationServiceReturnsSameInstance() {
        // Act
        ServiceFactory factory = ServiceFactory.getInstance();
        ReservationService service1 = factory.getReservationService();
        ReservationService service2 = factory.getReservationService();
        
        // Assert
        assertSame(service1, service2, "ServiceFactory should return same instance (Singleton)");
    }
    
    // Test 2: Service not null
    @Test
    void testGetReservationServiceNotNull() {
        // Act
        ServiceFactory factory = ServiceFactory.getInstance();
        ReservationService service = factory.getReservationService();
        
        // Assert
        assertNotNull(service, "ReservationService should not be null");
    }
    
    // Test 3: ReportsService singleton
    @Test
    void testGetReportsServiceReturnsSameInstance() {
        // Act
        ServiceFactory factory = ServiceFactory.getInstance();
        ReportsService service1 = factory.getReportsService();
        ReportsService service2 = factory.getReportsService();
        
        // Assert
        assertSame(service1, service2, "ReportsService should be singleton");
    }
    
    // Test 4: ReportsService not null
    @Test
    void testGetReportsServiceNotNull() {
        // Act
        ServiceFactory factory = ServiceFactory.getInstance();
        ReportsService service = factory.getReportsService();
        
        // Assert
        assertNotNull(service, "ReportsService should not be null");
    }
    
    // Test 5: Different service types
    @Test
    void testDifferentServiceTypes() {
        // Act
        ServiceFactory factory = ServiceFactory.getInstance();
        ReservationService reservationService = factory.getReservationService();
        ReportsService reportsService = factory.getReportsService();
        
        // Assert
        assertNotSame(reservationService, reportsService, "Different service types should be different instances");
    }
}
