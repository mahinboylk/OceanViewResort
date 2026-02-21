package com.oceanview.controller;

import com.oceanview.config.DBConnection;
import com.oceanview.model.Reservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ── POST → add | cancel ────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) { response.sendRedirect("login.jsp"); return; }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addReservation(request, response);
        } else if ("cancel".equals(action)) {
            cancelReservation(request, response);
        } else {
            response.sendRedirect("reservation?action=list");
        }
    }

    // ── GET → view | list ─────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) { response.sendRedirect("login.jsp"); return; }

        String action = request.getParameter("action");
        if ("view".equals(action)) {
            viewReservation(request, response);
        } else if ("list".equals(action)) {
            listReservations(request, response);
        } else if ("cancel".equals(action)) {
            cancelReservation(request, response);
        } else {
            response.sendRedirect("reservation?action=list");
        }
    }

    // ── Check Room Availability ───────────────────────────────────────
    private boolean isRoomAvailable(Connection conn, String roomType, Date checkIn, Date checkOut)
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE room_type = ? " +
                     "AND status = 'Active' " +
                     "AND NOT (check_out <= ? OR check_in >= ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, roomType);
        ps.setDate(2, checkIn);
        ps.setDate(3, checkOut);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0;
        }
        return true;
    }

    // ── Add reservation ───────────────────────────────────────
    private void addReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String guestName   = request.getParameter("guestName");
        String address     = request.getParameter("address");
        String contact     = request.getParameter("contact");
        String roomType    = request.getParameter("roomType");
        String checkInStr  = request.getParameter("checkIn");
        String checkOutStr = request.getParameter("checkOut");

        if (guestName == null || guestName.isBlank()
                || checkInStr == null || checkInStr.isBlank()
                || checkOutStr == null || checkOutStr.isBlank()) {
            response.sendRedirect("add_reservation.jsp?error=true");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            Date checkIn  = Date.valueOf(checkInStr);
            Date checkOut = Date.valueOf(checkOutStr);

            if (!checkOut.after(checkIn)) {
                response.sendRedirect("add_reservation.jsp?error=date");
                return;
            }

            if (!isRoomAvailable(conn, roomType, checkIn, checkOut)) {
                response.sendRedirect("add_reservation.jsp?error=unavailable");
                return;
            }

            String sql = "INSERT INTO reservations "
                    + "(guest_name, address, contact_number, room_type, check_in, check_out, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'Active')";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, guestName.trim());
            ps.setString(2, address);
            ps.setString(3, contact);
            ps.setString(4, roomType);
            ps.setDate(5, checkIn);
            ps.setDate(6, checkOut);
            ps.executeUpdate();

            response.sendRedirect("reservation?action=list&msg=success");

        } catch (IllegalArgumentException e) {
            response.sendRedirect("add_reservation.jsp?error=date");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("add_reservation.jsp?error=true");
        }
    }

    // ── Cancel reservation ───────────────────────────────────────
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "UPDATE reservations SET status = 'Cancelled' WHERE reservation_id = ? AND status = 'Active'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("reservation?action=view&id=" + id + "&msg=cancelled");
            } else {
                response.sendRedirect("reservation?action=list&error=notfound");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("reservation?action=list&error=true");
        }
    }

    // ── View single reservation ───────────────────────────────────────
    private void viewReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect("reservation?action=list");
            return;
        }

        Reservation res = null;

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                res = new Reservation();
                res.setReservationId(rs.getInt("reservation_id"));
                res.setGuestName(rs.getString("guest_name"));
                res.setAddress(rs.getString("address"));
                res.setContactNumber(rs.getString("contact_number"));
                res.setRoomType(rs.getString("room_type"));
                res.setCheckIn(rs.getDate("check_in"));
                res.setCheckOut(rs.getDate("check_out"));
                res.setStatus(rs.getString("status"));

                long diff   = res.getCheckOut().getTime() - res.getCheckIn().getTime();
                long nights = Math.max(1, diff / (1000L * 60 * 60 * 24));
                res.setTotalAmount(nights * getRoomRate(res.getRoomType()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res == null) { response.sendRedirect("reservation?action=list"); return; }

        request.setAttribute("reservation", res);
        request.getRequestDispatcher("view_reservation.jsp").forward(request, response);
    }

    // ── List all → forward to dashboard ──────────────────────
    private void listReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Reservation> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM reservations ORDER BY reservation_id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql);

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservation_id"));
                r.setGuestName(rs.getString("guest_name"));
                r.setRoomType(rs.getString("room_type"));
                r.setCheckIn(rs.getDate("check_in"));
                r.setStatus(rs.getString("status"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("resList", list);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    // ── LKR Room Rates ───────────────────────────────────────
    private double getRoomRate(String type) {
        if (type == null) return 25000.00;
        switch (type.toLowerCase().trim()) {
            case "ocean view": return 75000.00;
            case "suite":      return 55000.00;
            case "deluxe":     return 35000.00;
            default:           return 25000.00; // Standard
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}