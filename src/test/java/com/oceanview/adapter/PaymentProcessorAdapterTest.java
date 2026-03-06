package com.oceanview.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PaymentProcessorAdapter
 * Tests Adapter pattern implementation
 */
public class PaymentProcessorAdapterTest {
    
    private ExternalPaymentSystem externalSystem;
    private PaymentProcessorAdapter adapter;
    
    @BeforeEach
    void setUp() {
        externalSystem = mock(ExternalPaymentSystem.class);
        adapter = new PaymentProcessorAdapter(externalSystem);
    }
    
    // Test 1: Charge reservation successfully
    @Test
    void testChargeReservationSuccess() {
        // Arrange
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(true);
        
        // Act
        String transactionId = adapter.chargeReservation(1, 1000.0);
        
        // Assert
        assertNotNull(transactionId, "Transaction ID should be returned on success");
        assertTrue(transactionId.startsWith("TXN-"), "Transaction ID should start with TXN-");
        verify(externalSystem).processPayment(eq(1000.0), eq("USD"), anyString());
    }
    
    // Test 2: Charge reservation failure
    @Test
    void testChargeReservationFailure() {
        // Arrange
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(false);
        
        // Act
        String transactionId = adapter.chargeReservation(1, 1000.0);
        
        // Assert
        assertNull(transactionId, "Null should be returned on failure");
    }
    
    // Test 3: Cancel charge (refund) success
    @Test
    void testCancelChargeSuccess() {
        // Arrange - first charge to get transaction ID
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(true);
        adapter.chargeReservation(1, 1000.0);
        
        // Now setup refund
        when(externalSystem.refundPayment(anyString())).thenReturn(true);
        
        // Act
        boolean result = adapter.cancelCharge(1);
        
        // Assert
        assertTrue(result, "Refund should be successful");
    }
    
    // Test 4: Cancel charge (refund) failure - no transaction found
    @Test
    void testCancelChargeFailureNoTransaction() {
        // Act - try to cancel a reservation that was never charged
        boolean result = adapter.cancelCharge(999);
        
        // Assert
        assertFalse(result, "Refund should fail if no transaction found");
    }
    
    // Test 5: Get payment status
    @Test
    void testGetPaymentStatus() {
        // Arrange - first make a charge
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(true);
        adapter.chargeReservation(1, 1000.0);
        
        // Act
        String status = adapter.getPaymentStatus(1);
        
        // Assert
        assertNotNull(status);
        assertTrue(status.equals("PAID") || status.equals("PENDING") || status.equals("UNKNOWN"));
    }
}
