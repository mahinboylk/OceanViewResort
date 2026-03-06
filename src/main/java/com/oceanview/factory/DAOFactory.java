package com.oceanview.factory;

import com.oceanview.dao.*;

/**
 * DAO Factory - Creates Data Access Objects
 * 
 * DESIGN PATTERN: Factory Method
 * - Centralizes creation of DAO objects
 * - Decouples client code from concrete DAO implementations
 * - Easy to switch implementations (e.g., for testing)
 * 
 * PRINCIPLE: Open/Closed Principle (OCP)
 * - Open for extension (add new DAO types)
 * - Closed for modification (existing code doesn't change)
 * 
 * PRINCIPLE: Dependency Inversion Principle (DIP)
 * - High-level modules depend on abstractions (interfaces)
 * - Factory provides concrete implementations
 */
public class DAOFactory {

    // Singleton instance of factory
    private static DAOFactory instance;

    // Private constructor for singleton
    private DAOFactory() {}

    /**
     * Get singleton factory instance
     * @return DAOFactory instance
     */
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    /**
     * Create ReservationDAO implementation
     * @return ReservationDAO instance
     */
    public ReservationDAO createReservationDAO() {
        return new ReservationDAOImpl();
    }

    /**
     * Create UserDAO implementation
     * @return UserDAO instance
     */
    public UserDAO createUserDAO() {
        return new UserDAOImpl();
    }

    /**
     * Create ReportsDAO implementation
     * @return ReportsDAO instance
     */
    public ReportsDAO createReportsDAO() {
        return new ReportsDAOImpl();
    }
}
