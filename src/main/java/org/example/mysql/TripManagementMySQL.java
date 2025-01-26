package org.example.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripManagementMySQL {

    // MySQL database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/TravelPlanner";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Use your actual password

    // Method to insert a new trip
    public static boolean insertTrip(int userId, String destination, Date startDate, Date endDate) {
        String sql = "INSERT INTO trips (user_id, destination, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, destination);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update an existing trip
    public static boolean updateTrip(int tripId, String destination, Date startDate, Date endDate) {
        String sql = "UPDATE trips SET destination = ?, start_date = ?, end_date = ? WHERE trip_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, destination);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            stmt.setInt(4, tripId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to delete a trip
    public static boolean deleteTrip(int tripId) {
        String sql = "DELETE FROM trips WHERE trip_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve all trips for a specific user
    public static List<String[]> viewTrips(int userId) {
        List<String[]> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] trip = new String[4];
                trip[0] = String.valueOf(rs.getInt("trip_id"));
                trip[1] = rs.getString("destination");
                trip[2] = rs.getDate("start_date").toString();
                trip[3] = rs.getDate("end_date").toString();
                trips.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }

    // Method to retrieve all trips from the database
    public static List<String[]> getAllTrips() {
        List<String[]> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String[] trip = new String[4];
                trip[0] = String.valueOf(rs.getInt("trip_id"));
                trip[1] = rs.getString("destination");
                trip[2] = rs.getDate("start_date").toString();
                trip[3] = rs.getDate("end_date").toString();
                trips.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }

    // Method to search for trips by destination
    public static List<String[]> searchTripsByDestination(String destination) {
        List<String[]> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE destination LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + destination + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] trip = new String[4];
                trip[0] = String.valueOf(rs.getInt("trip_id"));
                trip[1] = rs.getString("destination");
                trip[2] = rs.getDate("start_date").toString();
                trip[3] = rs.getDate("end_date").toString();
                trips.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }

    // Method to retrieve a trip by its ID
    public static String[] getTripById(int tripId) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tripId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String[] trip = new String[4];
                trip[0] = String.valueOf(rs.getInt("trip_id"));
                trip[1] = rs.getString("destination");
                trip[2] = rs.getDate("start_date").toString();
                trip[3] = rs.getDate("end_date").toString();
                return trip;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no trip is found
    }
}
