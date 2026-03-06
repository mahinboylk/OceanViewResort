package com.oceanview.dao;

import java.sql.*;

/**
 * User DAO Implementation
 * 
 * DESIGN PATTERN: Singleton (uses DBConnection singleton)
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for user data access
 * 
 * 2. Interface Segregation Principle (ISP)
 *    - Implements only UserDAO interface methods
 * 
 * 3. Liskov Substitution Principle (LSP)
 *    - Can substitute UserDAO interface without breaking behavior
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public boolean validateUser(String username, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        // Use singleton DBConnection
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username.trim());
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
