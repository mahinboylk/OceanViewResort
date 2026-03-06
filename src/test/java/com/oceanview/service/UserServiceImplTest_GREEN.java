package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GREEN PHASE - This test will PASS
 * TDD Step 2: After implementation, the test passes
 */
public class UserServiceImplTest_GREEN {
    
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
        
        // Assert: Verify authentication succeeds with valid credentials
        assertTrue(actualResult, 
            "GREEN PHASE: Authentication with valid credentials (" + username + "/" + password + 
            ") should return true. Actual result: " + actualResult);
    }
}
