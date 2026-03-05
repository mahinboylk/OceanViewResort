package com.oceanview.adapter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class PaymentProcessorAdapterTest_GREEN {
    
    @Test
    void testChargeReservationSuccess() {
        // Arrange: Setup mock external payment system
        ExternalPaymentSystem externalSystem = mock(ExternalPaymentSystem.class);
        PaymentProcessorAdapter adapter = new PaymentProcessorAdapter(externalSystem);
        
        when(externalSystem.processPayment(anyDouble(), anyString(), anyString())).thenReturn(true);
        
        // Act: Charge a reservation
        String actualTransactionId = adapter.chargeReservation(1, 1000.0);
        
        // Assert: Verify transaction ID is returned and starts with TXN-
        assertNotNull(actualTransactionId, 
            "GREEN PHASE: Transaction ID should not be null when payment succeeds");
        assertTrue(actualTransactionId.startsWith("TXN-"), 
            "GREEN PHASE: Transaction ID should start with TXN-. Actual: " + actualTransactionId);
    }
}
