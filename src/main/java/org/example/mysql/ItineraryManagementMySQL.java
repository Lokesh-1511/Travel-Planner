package org.example.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItineraryManagementMySQL {

    public static boolean addItinerary(int userId, int tripId, Date date, String activity) {
        String query = "INSERT INTO itinerary (user_id, trip_id, date, activity) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TravelPlanner", "root", "root");
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, tripId);
            stmt.setDate(3, date);
            stmt.setString(4, activity);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getItineraryForTrip(int userId, int tripId) {
        List<String> itinerary = new ArrayList<>();
        String query = "SELECT date, activity FROM itinerary WHERE user_id = ? AND trip_id = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TravelPlanner", "root", "root");
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, tripId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                String activity = rs.getString("activity");
                itinerary.add(date + ": " + activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itinerary;
    }
}
