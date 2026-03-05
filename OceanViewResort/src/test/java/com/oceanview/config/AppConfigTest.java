package com.oceanview.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AppConfig Singleton
 * Tests configuration management and singleton pattern
 */
public class AppConfigTest {
    
    // Test 1: Singleton instance
    @Test
    void testGetInstanceReturnsSameInstance() {
        // Act
        AppConfig instance1 = AppConfig.getInstance();
        AppConfig instance2 = AppConfig.getInstance();
        
        // Assert
        assertSame(instance1, instance2, "AppConfig should be singleton");
    }
    
    // Test 2: Instance not null
    @Test
    void testGetInstanceNotNull() {
        // Act
        AppConfig config = AppConfig.getInstance();
        
        // Assert
        assertNotNull(config, "AppConfig instance should not be null");
    }
    
    // Test 3: Get API key (may be empty if not configured)
    @Test
    void testGetApiKey() {
        // Arrange
        AppConfig config = AppConfig.getInstance();
        
        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> config.getApiKey());
    }
    
    // Test 4: Get API URL returns default
    @Test
    void testGetApiUrl() {
        // Arrange
        AppConfig config = AppConfig.getInstance();
        
        // Act
        String apiUrl = config.getApiUrl();
        
        // Assert - should return default URL
        assertNotNull(apiUrl);
        assertTrue(apiUrl.contains("openrouter.ai"));
    }
    
    // Test 5: Get model returns default
    @Test
    void testGetModel() {
        // Arrange
        AppConfig config = AppConfig.getInstance();
        
        // Act
        String model = config.getModel();
        
        // Assert - should return default model
        assertNotNull(model);
        assertFalse(model.isEmpty());
    }
    
    // Test 6: Get app name returns default
    @Test
    void testGetAppName() {
        // Arrange
        AppConfig config = AppConfig.getInstance();
        
        // Act
        String appName = config.getAppName();
        
        // Assert - should return default app name
        assertNotNull(appName);
        assertEquals("Ocean View Resort", appName);
    }
    
    // Test 7: isApiKeyConfigured works
    @Test
    void testIsApiKeyConfigured() {
        // Arrange
        AppConfig config = AppConfig.getInstance();
        
        // Act & Assert - should not throw
        assertDoesNotThrow(() -> config.isApiKeyConfigured());
    }
}
