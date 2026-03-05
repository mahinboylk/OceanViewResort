package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * RED PHASE - This test will FAIL initially
 * TDD Step 1: Write a failing test before implementing the feature
 */
public class UserServiceImplTest_RED {
    
    @Test
    void testAuthenticateWithValidCredentials() throws Exception {
        // Arrange: Setup mock DAO with valid credentials
        UserDAO userDAO = mock(UserDAO.class);
        UserServiceImpl userService = new UserServiceImpl(userDAO);
        String username = "admin";
        String password = "password123";
        when(userDAO.validateUser(username, password)).thenReturn(true);
        
        // Act: Attempt authentication
        boolean actualResult = userService.authenticate(username, password);
        
        // Assert: Verify authentication result
        // This assertion uses WRONG expected value to demonstrate RED phase
        // Actual result is true (valid credentials), but we expect false
        assertFalse(actualResult, 
            "RED PHASE: Authentication should return WRONG value (false) to demonstrate TDD. " +
            "Actual result is " + actualResult + " because credentials are valid");
    }
}
