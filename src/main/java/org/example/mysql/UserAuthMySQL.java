package org.example.mysql;

import java.sql.*;

public class UserAuthMySQL {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/TravelPlanner";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    // Method to handle user login
    public static int login(String email, String password) {
        int userId = -1;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT user_id FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("user_id"); // Changed 'id' to 'user_id'
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    // Method to handle user registration
    public static int register(String email, String password) {
        int userId = -1;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, email);
                statement.setString(2, password);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1); // Get the generated user_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
