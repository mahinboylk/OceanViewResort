package com.oceanview.service;

/**
 * Service interface for User business logic
 */
public interface UserService {
    
    /**
     * Authenticate a user
     * @param username The username
     * @param password The password
     * @return true if authentication successful
     * @throws Exception if database error
     */
    boolean authenticate(String username, String password) throws Exception;
}
