package com.oceanview.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application Configuration Manager
 * 
 * DESIGN PATTERN: Singleton
 * - Single instance throughout the application
 * - Thread-safe lazy initialization
 * 
 * PRINCIPLE: Security
 * - API keys are loaded from properties file, never hardcoded
 * - Sensitive configuration is externalized
 * 
 * PRINCIPLE: Single Responsibility
 * - Only responsible for managing application configuration
 */
public class AppConfig {
    
    private static AppConfig instance;
    private Properties properties;
    
    /**
     * Private constructor for Singleton pattern
     */
    private AppConfig() {
        properties = new Properties();
        loadProperties();
    }
    
    /**
     * Get singleton instance
     * Thread-safe via synchronized
     * @return AppConfig instance
     */
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    /**
     * Load properties from config file
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                System.err.println("WARNING: config.properties not found. Using defaults.");
                setDefaults();
                return;
            }
            
            properties.load(input);
            
        } catch (IOException e) {
            System.err.println("ERROR loading config.properties: " + e.getMessage());
            setDefaults();
        }
    }
    
    /**
     * Set default values if config file not found
     */
    private void setDefaults() {
        properties.setProperty("openrouter.api.key", "");
        properties.setProperty("openrouter.api.url", "https://openrouter.ai/api/v1/chat/completions");
        properties.setProperty("openrouter.model", "google/gemini-2.0-flash-exp:free");
        properties.setProperty("app.name", "Ocean View Resort");
        properties.setProperty("app.support.email", "support@oceanviewresort.lk");
        properties.setProperty("app.url", "http://localhost:8080/OceanViewResort");
    }
    
    /**
     * Get OpenRouter API key
     * @return API key or empty string if not configured
     */
    public String getApiKey() {
        return properties.getProperty("openrouter.api.key", "");
    }
    
    /**
     * Check if API key is configured
     * @return true if API key is set
     */
    public boolean isApiKeyConfigured() {
        String key = getApiKey();
        return key != null && !key.isEmpty() && !key.equals("YOUR_API_KEY_HERE");
    }
    
    /**
     * Get OpenRouter API URL
     * @return API URL
     */
    public String getApiUrl() {
        return properties.getProperty("openrouter.api.url", 
            "https://openrouter.ai/api/v1/chat/completions");
    }
    
    /**
     * Get AI model to use
     * @return Model identifier
     */
    public String getModel() {
        return properties.getProperty("openrouter.model", 
            "google/gemini-2.0-flash-exp:free");
    }
    
    /**
     * Get application name
     * @return Application name
     */
    public String getAppName() {
        return properties.getProperty("app.name", "Ocean View Resort");
    }
    
    /**
     * Get support email
     * @return Support email address
     */
    public String getSupportEmail() {
        return properties.getProperty("app.support.email", "support@oceanviewresort.lk");
    }
    
    /**
     * Get application URL
     * @return Application base URL
     */
    public String getAppUrl() {
        return properties.getProperty("app.url", "http://localhost:8080/OceanViewResort");
    }
    
    /**
     * Reload configuration from file
     * Useful for dynamic configuration updates
     */
    public void reload() {
        loadProperties();
    }
}
