package com.oceanview.dao;

import com.oceanview.model.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation DAO Implementation
 * 
 * DESIGN PATTERN: Singleton (uses DBConnection singleton)
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for reservation data access
 * 
 * 2. Interface Segregation Principle (ISP)
 *    - Implements only ReservationDAO interface methods
 * 
 * 3. Dependency Inversion Principle (DIP)
 *    - Uses DBConnection abstraction, not concrete implementation
 */
public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public boolean insert(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservations " +
                     "(guest_name, address, contact_number, room_type, check_in, check_out, status, total_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Use singleton DBConnection
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, reservation.getGuestName());
            ps.setString(2, reservation.getAddress());
            ps.setString(3, reservation.getContactNumber());
            ps.setString(4, reservation.getRoomType());
            ps.setDate(5, reservation.getCheckIn());
            ps.setDate(6, reservation.getCheckOut());
            ps.setString(7, reservation.getStatus() != null ? reservation.getStatus() : "Active");
            ps.setDouble(8, reservation.getTotalAmount());
            
            int rows = ps.executeUpdate();
            
            // Get generated ID
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    reservation.setReservationId(rs.getInt(1));
                }
            }
            
            return rows > 0;
        }
    }

    @Override
    public boolean cancel(int reservationId) throws Exception {
        String sql = "UPDATE reservations SET status = 'Cancelled' " +
                     "WHERE reservation_id = ? AND status = 'Active'";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    @Override
    public Reservation findById(int reservationId) throws Exception {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            return null;
        }
    }

    @Override
    public List<Reservation> findAll() throws Exception {
        String sql = "SELECT * FROM reservations ORDER BY reservation_id DESC";
        List<Reservation> list = new ArrayList<>();
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(mapResultSetToReservation(rs));
            }
        }
        return list;
    }

    @Override
    public boolean isRoomAvailable(String roomType, Date checkIn, Date checkOut) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE room_type = ? " +
                     "AND status = 'Active' " +
                     "AND ( " +
                     "    (check_in <= ? AND check_out > ?) OR " +
                     "    (check_in < ? AND check_out >= ?) OR " +
                     "    (check_in >= ? AND check_out <= ?) " +
                     ")";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomType);
            ps.setDate(2, checkIn);
            ps.setDate(3, checkIn);
            ps.setDate(4, checkOut);
            ps.setDate(5, checkOut);
            ps.setDate(6, checkIn);
            ps.setDate(7, checkOut);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }
    
    /**
     * Helper method to map ResultSet to Reservation object
     * Single Responsibility: Only handles object mapping
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setGuestName(rs.getString("guest_name"));
        r.setAddress(rs.getString("address"));
        r.setContactNumber(rs.getString("contact_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setCheckIn(rs.getDate("check_in"));
        r.setCheckOut(rs.getDate("check_out"));
        r.setStatus(rs.getString("status"));
        r.setTotalAmount(rs.getDouble("total_amount"));
        return r;
    }
}
