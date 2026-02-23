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
import java.util.List;
import java.util.Map;

/**
 * Reports Servlet - Handles management reports
 *
 * DESIGN PATTERNS APPLIED:
 *
 * 1. Factory Pattern - Uses ServiceFactory to get service instances
 *    - Decoupled from concrete service implementations
 *
 * PRINCIPLES APPLIED:
 *
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for handling reports HTTP requests
 *
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on ReportsService interface, not implementation
 */
@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ReportsService reportsService;

    @Override
    public void init() throws ServletException {
        // Use Factory pattern to get service instance
        reportsService = ServiceFactory.getInstance().getReportsService();
    }

    /**
     * Handle GET reports request
     * Single Responsibility: Only handles reports page display
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Get all report data using service
            double totalRevenue              = reportsService.getTotalRevenue();
            int    activeReservations        = reportsService.getActiveReservationCount();
            int    cancelledReservations     = reportsService.getCancelledReservationCount();
            int    completedReservations     = reportsService.getCompletedReservationCount();
            int    currentOccupancy          = reportsService.getCurrentOccupancy();
            double occupancyRate             = reportsService.getOccupancyRate();
            List<Map<String, Object>> upcomingCheckins  = reportsService.getUpcomingCheckins();
            Map<String, Double>       revenueByRoomType = reportsService.getRevenueByRoomType();

            // Set attributes for JSP
            request.setAttribute("totalRevenue",          totalRevenue);
            request.setAttribute("activeReservations",    activeReservations);
            request.setAttribute("cancelledReservations", cancelledReservations);
            request.setAttribute("completedReservations", completedReservations);
            request.setAttribute("currentOccupancy",      currentOccupancy);
            request.setAttribute("occupancyRate",         occupancyRate);
            request.setAttribute("upcomingCheckins",      upcomingCheckins);
            request.setAttribute("revenueByRoomType",     revenueByRoomType);

            request.getRequestDispatcher("reports.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservation?action=list&error=report_error");
        }
    }

    /**
     * Check if user is logged in
     * Single Responsibility: Only handles authentication check
     */
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}