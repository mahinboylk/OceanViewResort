package com.oceanview.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class AppConfigTest_GREEN {
    
    @Test
    void testGetInstanceReturnsSameInstance() {
        // Arrange & Act: Get AppConfig instance twice
        AppConfig instance1 = AppConfig.getInstance();
        AppConfig instance2 = AppConfig.getInstance();
        
        // Assert: Verify singleton pattern
        assertSame(instance1, instance2, 
            "GREEN PHASE: AppConfig should return the same instance (Singleton pattern). " +
            "Both instances have hash: " + System.identityHashCode(instance1));
    }
}
