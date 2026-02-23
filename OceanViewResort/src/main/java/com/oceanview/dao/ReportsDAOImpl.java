package com.oceanview.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reports DAO Implementation
 * 
 * DESIGN PATTERN: Singleton (uses DBConnection singleton)
 * 
 * PRINCIPLES APPLIED:
 * 
 * 1. Single Responsibility Principle (SRP)
 *    - Only responsible for reports data access
 * 
 * 2. Interface Segregation Principle (ISP)
 *    - Implements only ReportsDAO interface methods
 * 
 * 3. Liskov Substitution Principle (LSP)
 *    - Can substitute ReportsDAO interface without breaking behavior
 */
public class ReportsDAOImpl implements ReportsDAO {

    @Override
    public double getTotalRevenue() throws Exception {
        double total = 0;
        String sql = "SELECT r.room_type, r.check_in, r.check_out " +
                     "FROM reservations r " +
                     "WHERE r.status IN ('Active', 'Completed')";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String roomType = rs.getString("room_type");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                
                if (checkIn != null && checkOut != null) {
                    long diff = checkOut.getTime() - checkIn.getTime();
                    long nights = Math.max(1, diff / (1000L * 60 * 60 * 24));
                    total += nights * getRoomRate(roomType);
                }
            }
        }
        return total;
    }

    @Override
    public int getCountByStatus(String status) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations WHERE status = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public int getCurrentOccupancy() throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE status = 'Active' " +
                     "AND check_in <= CURDATE() " +
                     "AND check_out >= CURDATE()";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> getUpcomingCheckins() throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT reservation_id, guest_name, room_type, check_in " +
                     "FROM reservations " +
                     "WHERE status = 'Active' " +
                     "AND check_in >= CURDATE() " +
                     "AND check_in <= DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                     "ORDER BY check_in ASC " +
                     "LIMIT 10";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("reservationId", rs.getInt("reservation_id"));
                map.put("guestName", rs.getString("guest_name"));
                map.put("roomType", rs.getString("room_type"));
                map.put("checkIn", rs.getDate("check_in"));
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public Map<String, Double> getRevenueByRoomType() throws Exception {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT room_type, " +
                     "SUM(CASE " +
                     "    WHEN room_type = 'Ocean View' THEN DATEDIFF(check_out, check_in) * 300 " +
                     "    WHEN room_type = 'Suite' THEN DATEDIFF(check_out, check_in) * 250 " +
                     "    WHEN room_type = 'Deluxe' THEN DATEDIFF(check_out, check_in) * 150 " +
                     "    ELSE DATEDIFF(check_out, check_in) * 100 " +
                     "END) as revenue " +
                     "FROM reservations " +
                     "WHERE status IN ('Active', 'Completed') " +
                     "GROUP BY room_type";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String roomType = rs.getString("room_type");
                Double revenue = rs.getDouble("revenue");
                if (roomType != null) {
                    map.put(roomType, revenue);
                }
            }
        }
        return map;
    }
    
    /**
     * Get room rate based on type
     * Single Responsibility: Only handles rate lookup
     */
    private double getRoomRate(String type) {
        if (type == null) return 100.00;
        switch (type.toLowerCase().trim()) {
            case "ocean view": return 300.00;
            case "suite":      return 250.00;
            case "deluxe":     return 150.00;
            default:           return 100.00;
        }
    }
}
