package org.example.mysql;

import java.sql.*;

public class BudgetTrackerMySQL {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/TravelPlanner";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getBudget(int userId, int tripId) {
        String query = "SELECT budget FROM budgets WHERE user_id = ? AND trip_id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tripId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getDouble("budget");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean setBudget(int userId, int tripId, double budget) {
        String query = "INSERT INTO budgets (user_id, trip_id, budget) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE budget = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, tripId);
            statement.setDouble(3, budget);
            statement.setDouble(4, budget);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean trackExpense(int userId, int tripId, double expense, String description) {
        String expenseQuery = "INSERT INTO expenses (user_id, trip_id, expense, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement expenseStatement = connection.prepareStatement(expenseQuery)) {
            expenseStatement.setInt(1, userId);
            expenseStatement.setInt(2, tripId);
            expenseStatement.setDouble(3, expense);
            expenseStatement.setString(4, description);
            expenseStatement.executeUpdate();

            String updateBudgetQuery = "UPDATE budgets SET budget = budget - ? WHERE user_id = ? AND trip_id = ?";
            try (PreparedStatement budgetStatement = connection.prepareStatement(updateBudgetQuery)) {
                budgetStatement.setDouble(1, expense);
                budgetStatement.setInt(2, userId);
                budgetStatement.setInt(3, tripId);
                return budgetStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
