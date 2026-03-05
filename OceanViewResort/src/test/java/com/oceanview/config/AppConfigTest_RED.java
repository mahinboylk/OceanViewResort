package com.oceanview.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class AppConfigTest_RED {
    
    @Test
    void testGetInstanceReturnsSameInstance() {
        // Arrange & Act: Get AppConfig instance twice
        AppConfig instance1 = AppConfig.getInstance();
        AppConfig instance2 = AppConfig.getInstance();
        
        // Assert: Verify singleton pattern
        // This assertion uses WRONG assertion to demonstrate RED phase
        // instance1 and instance2 ARE the same, but we assert they are NOT
        assertNotSame(instance1, instance2, 
            "RED PHASE: AppConfig instances should be different (WRONG for RED phase). " +
            "instance1 hash: " + System.identityHashCode(instance1) + 
            ", instance2 hash: " + System.identityHashCode(instance2));
    }
}
