package com.oceanview.adapter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class PaymentProcessorAdapterTest_RED {
    
    @Test
    void testChargeReservationSuccess() {
        // Arrange: Setup mock external payment system
        ExternalPaymentSystem externalSystem = mock(ExternalPaymentSystem.class);
        PaymentProcessorAdapter adapter = new PaymentProcessorAdapter(externalSystem);
        
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(true);
        
        // Act: Charge a reservation
        String actualTransactionId = adapter.chargeReservation(1, 1000.0);
        
        // Assert: Verify transaction ID is returned
        // This assertion uses WRONG expected value to demonstrate RED phase
        // Actual transactionId is NOT null (e.g., "TXN-XXXX"), but we expect null
        assertNull(actualTransactionId, 
            "RED PHASE: Transaction ID should be null (WRONG for RED phase). " +
            "Actual transactionId is: " + actualTransactionId);
    }
}
