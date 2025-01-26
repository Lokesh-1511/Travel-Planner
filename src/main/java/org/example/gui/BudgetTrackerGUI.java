package org.example.gui;

import org.example.mysql.BudgetTrackerMySQL;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BudgetTrackerGUI {
    private JPanel panel;
    private JTextField userIdField;
    private JTextField tripIdField;
    private JTextField budgetField;
    private JTextField expenseField;
    private JTextField expenseDescriptionField;
    private JLabel currentBudgetLabel;
    private Image backgroundImage;

    public BudgetTrackerGUI() {
        // Load background image
        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("center-image.jpg"))).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            backgroundImage = null; // Fallback if image fails to load
        }

        // Initialize UI with background image
        initializeUI();
    }

    private void initializeUI() {
        // Create custom JPanel with background
        panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create a panel with semi-transparent black background for the content
        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set the semi-transparent black background
                g.setColor(new Color(0, 0, 0, 120));  // RGBA: 120 is the transparency level
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setOpaque(false); // Set to false to avoid overwriting custom paintComponent

        // Title
        JLabel titleLabel = new JLabel("Budget Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);

        // User ID field
        addFieldWithLabel(contentPanel, gbc, "User ID: ", 1, userIdField = createStyledTextField());

        // Trip ID field
        addFieldWithLabel(contentPanel, gbc, "Trip ID: ", 2, tripIdField = createStyledTextField());

        // Show Budget Button
        JButton btnShowBudget = createStyledButton("Show Budget", new Color(0, 204, 102));
        btnShowBudget.addActionListener(e -> showBudget());
        addComponentToPanel(contentPanel, gbc, btnShowBudget, 3, 2, GridBagConstraints.HORIZONTAL);

        // Budget field
        addFieldWithLabel(contentPanel, gbc, "Set Budget: ", 4, budgetField = createStyledTextField());

        // Set Budget Button
        JButton btnSetBudget = createStyledButton("Set Budget", new Color(0, 204, 102));
        btnSetBudget.addActionListener(e -> setBudget());
        addComponentToPanel(contentPanel, gbc, btnSetBudget, 5, 2, GridBagConstraints.HORIZONTAL);

        // Expense field
        addFieldWithLabel(contentPanel, gbc, "Track Expense: ", 6, expenseField = createStyledTextField());

        // Expense Description field
        addFieldWithLabel(contentPanel, gbc, "Description: ", 7, expenseDescriptionField = createStyledTextField());

        // Track Expense Button
        JButton btnTrackExpense = createStyledButton("Track Expense", new Color(204, 0, 51));
        btnTrackExpense.addActionListener(e -> trackExpense());
        addComponentToPanel(contentPanel, gbc, btnTrackExpense, 8, 2, GridBagConstraints.HORIZONTAL);

        // Current Budget Label
        currentBudgetLabel = new JLabel("Current Budget: $0.00");
        currentBudgetLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentBudgetLabel.setForeground(new Color(255, 255, 255));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(currentBudgetLabel, gbc);

        // Add content panel to the main panel
        panel.add(contentPanel, gbc);
    }

    private void addFieldWithLabel(JPanel contentPanel, GridBagConstraints gbc, String labelText, int row, JTextField textField) {
        JLabel label = createStyledLabel(labelText);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(textField, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.white);
        return label;
    }

    private void addComponentToPanel(JPanel contentPanel, GridBagConstraints gbc, Component component, int row, int width, int fill) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = width;
        gbc.fill = fill;
        contentPanel.add(component, gbc);
    }

    private void showBudget() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            int tripId = Integer.parseInt(tripIdField.getText());
            double budget = BudgetTrackerMySQL.getBudget(userId, tripId);
            currentBudgetLabel.setText("Current Budget: $" + String.format("%.2f", budget));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Please enter valid numeric values for User ID and Trip ID.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error retrieving budget: " + e.getMessage());
        }
    }

    private void setBudget() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            int tripId = Integer.parseInt(tripIdField.getText());
            double budget = Double.parseDouble(budgetField.getText());
            if (BudgetTrackerMySQL.setBudget(userId, tripId, budget)) {
                currentBudgetLabel.setText("Current Budget: $" + String.format("%.2f", budget));
                JOptionPane.showMessageDialog(panel, "Budget set successfully!");
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to set budget.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Please enter valid numeric values for User ID, Trip ID, and Budget.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error setting budget: " + e.getMessage());
        }
    }

    private void trackExpense() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            int tripId = Integer.parseInt(tripIdField.getText());
            double expense = Double.parseDouble(expenseField.getText());
            String description = expenseDescriptionField.getText();

            if (BudgetTrackerMySQL.trackExpense(userId, tripId, expense, description)) {
                double updatedBudget = BudgetTrackerMySQL.getBudget(userId, tripId);
                currentBudgetLabel.setText("Current Budget: $" + String.format("%.2f", updatedBudget));
                JOptionPane.showMessageDialog(panel, "Expense tracked successfully!");
            } else {
                JOptionPane.showMessageDialog(panel, "Failed to track expense.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Please enter valid numeric values for User ID, Trip ID, and Expense.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Error tracking expense: " + e.getMessage());
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
