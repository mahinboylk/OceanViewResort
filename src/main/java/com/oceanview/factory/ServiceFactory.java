package com.oceanview.factory;

import com.oceanview.service.*;
// Removed: import com.oceanview.dao.*; (unused)

public class ServiceFactory {
    private static ServiceFactory instance;
    private ReservationService reservationService;
    private UserService userService;
    private ReportsService reportsService;
    
    private ServiceFactory() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.reservationService = new ReservationServiceImpl(daoFactory.createReservationDAO());
        this.userService = new UserServiceImpl(daoFactory.createUserDAO());
        this.reportsService = new ReportsServiceImpl(daoFactory.createReportsDAO());
    }
    
    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }
    
    public ReservationService getReservationService() {
        return reservationService;
    }
    
    public UserService getUserService() {
        return userService;
    }
    
    public ReportsService getReportsService() {
        return reportsService;
    }
}