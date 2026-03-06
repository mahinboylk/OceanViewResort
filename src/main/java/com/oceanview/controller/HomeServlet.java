package com.oceanview.controller;

import com.oceanview.factory.ServiceFactory;
import com.oceanview.service.ReportsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Home Servlet - Handles home page display with user dashboard
 * 
 * DESIGN PATTERNS APPLIED:
 * 
 * 1. Factory Pattern - Uses ServiceFactory to get service instances
 *    - Decoupled from concrete service implementations
 *    - ServiceFactory uses Singleton pattern internally
 * 
 * 2. Singleton Pattern - Indirectly through ServiceFactory
 *    - Single instance of services reused across requests
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for preparing home page data
 *    - Does not contain business logic (delegated to services)
 * 
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on ReportsService interface, not implementation
 * 
 * 3. Open/Closed Principle (OCP)
 *    - Can add new statistics without modifying existing code
 *    - New features can be added through service extensions
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ReportsService reportsService;
    
    // Total rooms constant - follows SRP (configuration separate from logic)
    private static final int TOTAL_ROOMS = 20;

    @Override
    public void init() throws ServletException {
        // Use Factory pattern to get service instance
        // Factory internally uses Singleton for service management
        reportsService = ServiceFactory.getInstance().getReportsService();
    }

    /**
     * Handle GET home request
     * 
     * Single Responsibility: Only handles home page data preparation
     * Business logic is delegated to service layer
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth guard - separates authentication from business logic
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("user");

        try {
            // Get statistics using service (follows DIP - depends on abstraction)
            int activeReservations = reportsService.getActiveReservationCount();
            int currentOccupancy = reportsService.getCurrentOccupancy();
            double occupancyRate = (double) currentOccupancy / TOTAL_ROOMS * 100;

            // Set attributes for JSP view
            request.setAttribute("username", username);
            request.setAttribute("totalRooms", TOTAL_ROOMS);
            request.setAttribute("activeReservations", activeReservations);
            request.setAttribute("currentOccupancy", currentOccupancy);
            request.setAttribute("occupancyRate", String.format("%.1f", occupancyRate));

            // Forward to view - follows SRP (servlet handles controller logic, JSP handles presentation)
            request.getRequestDispatcher("home.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Graceful error handling
            request.setAttribute("error", "Unable to load dashboard statistics");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
