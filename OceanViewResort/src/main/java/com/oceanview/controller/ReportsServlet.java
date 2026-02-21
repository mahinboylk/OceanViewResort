package com.oceanview.controller;

import com.oceanview.config.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // LKR nightly rates
    private static final double RATE_OCEAN_VIEW = 75000.00;
    private static final double RATE_SUITE      = 55000.00;
    private static final double RATE_DELUXE     = 35000.00;
    private static final double RATE_STANDARD   = 25000.00;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        double totalRevenue            = getTotalRevenue();
        int    activeReservations      = getActiveReservationCount();
        int    cancelledReservations   = getCancelledReservationCount();
        int    completedReservations   = getCompletedReservationCount();
        int    currentOccupancy        = getCurrentOccupancy();
        double occupancyRate           = calculateOccupancyRate(currentOccupancy);
        List<Map<String, Object>> upcomingCheckins  = getUpcomingCheckins();
        List<Map<String, Object>> recentRevenue     = getRecentRevenue();
        Map<String, Double>       revenueByRoomType = getRevenueByRoomType();

        request.setAttribute("totalRevenue",          totalRevenue);
        request.setAttribute("activeReservations",    activeReservations);
        request.setAttribute("cancelledReservations", cancelledReservations);
        request.setAttribute("completedReservations", completedReservations);
        request.setAttribute("currentOccupancy",      currentOccupancy);
        request.setAttribute("occupancyRate",         occupancyRate);
        request.setAttribute("upcomingCheckins",      upcomingCheckins);
        request.setAttribute("recentRevenue",         recentRevenue);
        request.setAttribute("revenueByRoomType",     revenueByRoomType);

        request.getRequestDispatcher("reports.jsp").forward(request, response);
    }

    // ── Total Revenue (Active + Completed) ────────────────────────────
    private double getTotalRevenue() {
        double total = 0;
        String sql = "SELECT room_type, check_in, check_out FROM reservations WHERE status IN ('Active','Completed')";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String roomType = rs.getString("room_type");
                Date checkIn    = rs.getDate("check_in");
                Date checkOut   = rs.getDate("check_out");
                if (checkIn != null && checkOut != null) {
                    long nights = Math.max(1, (checkOut.getTime() - checkIn.getTime()) / (1000L * 60 * 60 * 24));
                    total += nights * getRoomRate(roomType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // ── Counts ────────────────────────────────────────────────
    private int getActiveReservationCount() {
        return queryCount("SELECT COUNT(*) FROM reservations WHERE status = 'Active'");
    }
    private int getCancelledReservationCount() {
        return queryCount("SELECT COUNT(*) FROM reservations WHERE status = 'Cancelled'");
    }
    private int getCompletedReservationCount() {
        return queryCount("SELECT COUNT(*) FROM reservations WHERE status = 'Completed'");
    }
    private int getCurrentOccupancy() {
        return queryCount("SELECT COUNT(*) FROM reservations WHERE status='Active' AND check_in<=CURDATE() AND check_out>=CURDATE()");
    }
    private int queryCount(String sql) {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    // ── Occupancy Rate ────────────────────────────────────────
    private double calculateOccupancyRate(int currentOccupancy) {
        return (double) currentOccupancy / 20 * 100;
    }

    // ── Upcoming Check-ins (next 7 days) ─────────────────────
    private List<Map<String, Object>> getUpcomingCheckins() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT reservation_id, guest_name, room_type, check_in FROM reservations " +
                     "WHERE status='Active' AND check_in>=CURDATE() AND check_in<=DATE_ADD(CURDATE(),INTERVAL 7 DAY) " +
                     "ORDER BY check_in ASC LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("reservationId", rs.getInt("reservation_id"));
                map.put("guestName",     rs.getString("guest_name"));
                map.put("roomType",      rs.getString("room_type"));
                map.put("checkIn",       rs.getDate("check_in"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Revenue by Month (last 6 months) — LKR rates in SQL ──
    private List<Map<String, Object>> getRecentRevenue() {
        List<Map<String, Object>> list = new ArrayList<>();
        // Rates updated to LKR: Ocean View 75000, Suite 55000, Deluxe 35000, Standard 25000
        String sql = "SELECT DATE_FORMAT(check_in,'%Y-%m') AS month, " +
                     "SUM(CASE " +
                     "  WHEN room_type='Ocean View' THEN DATEDIFF(check_out,check_in)*75000 " +
                     "  WHEN room_type='Suite'      THEN DATEDIFF(check_out,check_in)*55000 " +
                     "  WHEN room_type='Deluxe'     THEN DATEDIFF(check_out,check_in)*35000 " +
                     "  ELSE                             DATEDIFF(check_out,check_in)*25000 " +
                     "END) AS revenue " +
                     "FROM reservations WHERE status IN ('Active','Completed') " +
                     "AND check_in>=DATE_SUB(CURDATE(),INTERVAL 6 MONTH) " +
                     "GROUP BY DATE_FORMAT(check_in,'%Y-%m') ORDER BY month DESC LIMIT 6";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("month",   rs.getString("month"));
                map.put("revenue", rs.getDouble("revenue"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── Revenue by Room Type — LKR rates in SQL ───────────────
    private Map<String, Double> getRevenueByRoomType() {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT room_type, " +
                     "SUM(CASE " +
                     "  WHEN room_type='Ocean View' THEN DATEDIFF(check_out,check_in)*75000 " +
                     "  WHEN room_type='Suite'      THEN DATEDIFF(check_out,check_in)*55000 " +
                     "  WHEN room_type='Deluxe'     THEN DATEDIFF(check_out,check_in)*35000 " +
                     "  ELSE                             DATEDIFF(check_out,check_in)*25000 " +
                     "END) AS revenue " +
                     "FROM reservations WHERE status IN ('Active','Completed') GROUP BY room_type";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String roomType = rs.getString("room_type");
                if (roomType != null) map.put(roomType, rs.getDouble("revenue"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // ── LKR Room Rate Helper ──────────────────────────────────
    private double getRoomRate(String type) {
        if (type == null) return RATE_STANDARD;
        switch (type.toLowerCase().trim()) {
            case "ocean view": return RATE_OCEAN_VIEW;
            case "suite":      return RATE_SUITE;
            case "deluxe":     return RATE_DELUXE;
            default:           return RATE_STANDARD;
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}