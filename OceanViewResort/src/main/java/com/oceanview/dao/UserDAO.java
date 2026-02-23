package com.oceanview.dao;

/**
 * Data Access Object interface for User operations
 */
public interface UserDAO {
    
    /**
     * Validate user credentials
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
     */
    boolean validateUser(String username, String password) throws Exception;
}
