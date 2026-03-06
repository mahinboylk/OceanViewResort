package com.oceanview.controller;

import com.oceanview.factory.ServiceFactory;
import com.oceanview.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login Servlet - Handles user authentication
 * 
 * DESIGN PATTERNS APPLIED:
 * 
 * 1. Factory Pattern - Uses ServiceFactory to get service instances
 *    - Decoupled from concrete service implementations
 *    - ServiceFactory uses Singleton pattern internally
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for handling login HTTP requests
 * 
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on UserService interface, not implementation
 * 
 * 3. Open/Closed Principle (OCP)
 *    - Can add new authentication methods without modifying existing code
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        // Use Factory pattern to get service instance
        // Factory internally uses Singleton for service management
        userService = ServiceFactory.getInstance().getUserService();
    }

    /**
     * Handle POST login request
     * 
     * Single Responsibility: Only handles login form submission
     * Business logic is delegated to service layer (DIP)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Input validation - SRP: Controller handles input validation
        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            // Authenticate using service (DIP - depends on abstraction)
            if (userService.authenticate(username, password)) {
                // Create session on successful authentication
                HttpSession session = request.getSession(true);
                session.setAttribute("user", username.trim());
                session.setMaxInactiveInterval(3600);
                
                // Redirect to home servlet (which uses Factory, Strategy, Observer patterns)
                response.sendRedirect("home");
            } else {
                // Authentication failed
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=db_error");
        }
    }

    /**
     * Handle GET login request
     * 
     * Single Responsibility: Only handles login page display
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Already logged in, redirect to home
            response.sendRedirect("home");
        } else {
            // Not logged in, show login page
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
