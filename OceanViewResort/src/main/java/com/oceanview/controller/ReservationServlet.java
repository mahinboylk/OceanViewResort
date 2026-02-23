package com.oceanview.controller;

import com.oceanview.factory.ServiceFactory;
import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Reservation Servlet - Handles all reservation operations
 * 
 * DESIGN PATTERNS APPLIED:
 * 
 * 1. Factory Pattern - Uses ServiceFactory to get service instances
 *    - Decoupled from concrete service implementations
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for handling HTTP requests/responses
 * 
 * 2. Dependency Inversion Principle (DIP)
 *    - Depends on ReservationService interface, not implementation
 * 
 * 3. Open/Closed Principle (OCP)
 *    - Can add new actions without modifying existing code structure
 */
@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ReservationService reservationService;
    
    @Override
    public void init() throws ServletException {
        // Use Factory pattern to get service instance
        reservationService = ServiceFactory.getInstance().getReservationService();
    }

    // POST → add | cancel
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addReservation(request, response);
        } else if ("cancel".equals(action)) {
            cancelReservation(request, response);
        } else {
            response.sendRedirect("reservation?action=list");
        }
    }

    // GET → view | list | cancel
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("view".equals(action)) {
            viewReservation(request, response);
        } else if ("list".equals(action)) {
            listReservations(request, response);
        } else if ("cancel".equals(action)) {
            // Handle cancel via GET (from view_reservation.jsp link)
            cancelReservation(request, response);
        } else {
            response.sendRedirect("reservation?action=list");
        }
    }

    /**
     * Add a new reservation
     * Single Responsibility: Only handles add reservation request
     */
    private void addReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String guestName   = request.getParameter("guestName");
        String address     = request.getParameter("address");
        String contact     = request.getParameter("contact");
        String roomType    = request.getParameter("roomType");
        String checkInStr  = request.getParameter("checkIn");
        String checkOutStr = request.getParameter("checkOut");

        // Validate required fields
        if (guestName == null || guestName.isBlank()
                || checkInStr == null || checkInStr.isBlank()
                || checkOutStr == null || checkOutStr.isBlank()) {
            response.sendRedirect("add_reservation.jsp?error=true");
            return;
        }

        try {
            Date checkIn  = Date.valueOf(checkInStr);
            Date checkOut = Date.valueOf(checkOutStr);

            // Create reservation object
            Reservation reservation = new Reservation();
            reservation.setGuestName(guestName.trim());
            reservation.setAddress(address);
            reservation.setContactNumber(contact);
            reservation.setRoomType(roomType);
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);
            reservation.setStatus("Active");

            // Use service to create reservation (includes availability check)
            reservationService.createReservation(reservation);
            response.sendRedirect("reservation?action=list&msg=success");

        } catch (IllegalArgumentException e) {
            response.sendRedirect("add_reservation.jsp?error=date");
        } catch (IllegalStateException e) {
            // Room not available
            response.sendRedirect("add_reservation.jsp?error=unavailable");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("add_reservation.jsp?error=true");
        }
    }

    /**
     * Cancel a reservation
     * Single Responsibility: Only handles cancel reservation request
     */
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            boolean cancelled = reservationService.cancelReservation(id);

            if (cancelled) {
                // Redirect to dashboard with cancel success message
                response.sendRedirect("reservation?action=list&msg=cancelled");
            } else {
                response.sendRedirect("reservation?action=list&error=notfound");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("reservation?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservation?action=list&error=true");
        }
    }

    /**
     * View single reservation
     * Single Responsibility: Only handles view reservation request
     */
    private void viewReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Reservation res = reservationService.getReservation(id);

            if (res == null) {
                response.sendRedirect("reservation?action=list");
                return;
            }

            request.setAttribute("reservation", res);
            request.getRequestDispatcher("view_reservation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("reservation?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservation?action=list");
        }
    }

    /**
     * List all reservations
     * Single Responsibility: Only handles list reservations request
     */
    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Reservation> list = reservationService.getAllReservations();
            request.setAttribute("resList", list);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("resList", null);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
