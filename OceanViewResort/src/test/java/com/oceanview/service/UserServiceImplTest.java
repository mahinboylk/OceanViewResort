package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl
 * Uses Mockito to mock UserDAO dependency
 */
public class UserServiceImplTest {
    
    private UserServiceImpl userService;
    private UserDAO userDAO;
    
    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserServiceImpl(userDAO);
    }
    
    // Test 1: Valid credentials authentication
    @Test
    void testAuthenticateWithValidCredentials() throws Exception {
        // Arrange
        when(userDAO.validateUser("admin", "password123")).thenReturn(true);
        
        // Act
        boolean result = userService.authenticate("admin", "password123");
        
        // Assert
        assertTrue(result);
        verify(userDAO, times(1)).validateUser("admin", "password123");
    }
    
    // Test 2: Invalid credentials authentication
    @Test
    void testAuthenticateWithInvalidCredentials() throws Exception {
        // Arrange
        when(userDAO.validateUser("admin", "wrongpassword")).thenReturn(false);
        
        // Act
        boolean result = userService.authenticate("admin", "wrongpassword");
        
        // Assert
        assertFalse(result);
    }
    
    // Test 3: Empty username
    @Test
    void testAuthenticateWithEmptyUsername() throws Exception {
        // Act
        boolean result = userService.authenticate("", "password");
        
        // Assert
        assertFalse(result);
    }
    
    // Test 4: Empty password
    @Test
    void testAuthenticateWithEmptyPassword() throws Exception {
        // Act
        boolean result = userService.authenticate("admin", "");
        
        // Assert
        assertFalse(result);
    }
    
    // Test 5: Null credentials
    @Test
    void testAuthenticateWithNullCredentials() throws Exception {
        // Act
        boolean result = userService.authenticate(null, null);
        
        // Assert
        assertFalse(result);
    }
}
