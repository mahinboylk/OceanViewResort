package com.oceanview.service;

import com.oceanview.dao.UserDAO;

/**
 * User Service Implementation
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for user authentication business logic
 * 
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on UserDAO interface, not concrete implementation
 *    - DAO is injected through constructor
 * 
 * 3. Liskov Substitution Principle (LSP)
 *    - Can substitute UserService interface without breaking behavior
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    
    /**
     * Constructor with dependency injection
     * @param userDAO The DAO to use for data access
     */
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean authenticate(String username, String password) throws Exception {
        // Validate input - business logic
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // Delegate to DAO for data access
        return userDAO.validateUser(username.trim(), password);
    }
}
