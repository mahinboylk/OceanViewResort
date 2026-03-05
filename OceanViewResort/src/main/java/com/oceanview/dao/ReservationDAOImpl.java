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
                     "(guest_name, email, nic_passport, address, contact_number, room_type, check_in, check_out, status, total_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Use singleton DBConnection
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, reservation.getGuestName());
            ps.setString(2, reservation.getEmail());
            ps.setString(3, reservation.getNicPassport());
            ps.setString(4, reservation.getAddress());
            ps.setString(5, reservation.getContactNumber());
            ps.setString(6, reservation.getRoomType());
            ps.setDate(7, reservation.getCheckIn());
            ps.setDate(8, reservation.getCheckOut());
            ps.setString(9, reservation.getStatus() != null ? reservation.getStatus() : "Active");
            ps.setDouble(10, reservation.getTotalAmount());
            
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
        r.setEmail(rs.getString("email"));
        r.setNicPassport(rs.getString("nic_passport"));
        r.setAddress(rs.getString("address"));
        r.setContactNumber(rs.getString("contact_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setCheckIn(rs.getDate("check_in"));
        r.setCheckOut(rs.getDate("check_out"));
        r.setStatus(rs.getString("status"));
        r.setTotalAmount(rs.getDouble("total_amount"));
        return r;
    }

    @Override
    public List<Reservation> search(String keyword, String roomType, String status, Date checkIn) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM reservations WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        // Keyword search (guest name, email, contact, or reservation ID)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (guest_name LIKE ? OR email LIKE ? OR contact_number LIKE ? OR reservation_id = ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
            // Try to parse as integer for reservation ID
            try {
                params.add(Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                params.add(-1); // Invalid ID won't match anything
            }
        }
        
        // Room type filter
        if (roomType != null && !roomType.trim().isEmpty()) {
            sql.append(" AND room_type = ?");
            params.add(roomType);
        }
        
        // Status filter
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        
        // Check-in date filter
        if (checkIn != null) {
            sql.append(" AND check_in = ?");
            params.add(checkIn);
        }
        
        sql.append(" ORDER BY reservation_id DESC");
        
        List<Reservation> results = new ArrayList<>();
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof String) {
                    ps.setString(i + 1, (String) params.get(i));
                } else if (params.get(i) instanceof Integer) {
                    ps.setInt(i + 1, (Integer) params.get(i));
                } else if (params.get(i) instanceof Date) {
                    ps.setDate(i + 1, (Date) params.get(i));
                }
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToReservation(rs));
            }
        }
        
        return results;
    }
}
