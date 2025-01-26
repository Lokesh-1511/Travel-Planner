package org.example.gui;

import org.example.mysql.TripManagementMySQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class TripManagementGUI {
    private JPanel mainPanel;
    private JTextField txtUserId, destinationField, startDateField, endDateField, tripIdField;
    private Image backgroundImage;

    public TripManagementGUI() {
        // Load background image
        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("center-image.jpg"))
            ).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            backgroundImage = null; // Fallback if image fails to load
        }

        // Create custom JPanel to paint background
        mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Transparent components
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between components

        // Title
        JLabel titleLabel = createStyledLabel("Trip Management", 24, true);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Add input fields
        addLabelAndField("Trip ID (for Update/Delete):", tripIdField = createStyledTextField(), gbc, 1);
        addLabelAndField("Destination:", destinationField = createStyledTextField(), gbc, 2);
        addLabelAndField("Start Date (YYYY-MM-DD):", startDateField = createStyledTextField(), gbc, 3);
        addLabelAndField("End Date (YYYY-MM-DD):", endDateField = createStyledTextField(), gbc, 4);
        addLabelAndField("User ID:", txtUserId = createStyledTextField(), gbc, 5);

        // Add buttons
        addButtons(gbc);
    }

    private void addLabelAndField(String labelText, JTextField textField, GridBagConstraints gbc, int row) {
        JLabel label = createStyledLabel(labelText, 16, false);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(textField, gbc);
    }

    private void addButtons(GridBagConstraints gbc) {
        JButton btnAddTrip = createStyledButton("Add Trip", new Color(0, 204, 102));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(btnAddTrip, gbc);
        btnAddTrip.addActionListener(e -> addTrip());

        JButton btnUpdateTrip = createStyledButton("Update Trip", new Color(255, 204, 0));
        gbc.gridy = 7;
        mainPanel.add(btnUpdateTrip, gbc);
        btnUpdateTrip.addActionListener(e -> updateTrip());

        JButton btnDeleteTrip = createStyledButton("Delete Trip", new Color(255, 51, 51));
        gbc.gridy = 8;
        mainPanel.add(btnDeleteTrip, gbc);
        btnDeleteTrip.addActionListener(e -> deleteTrip());

        JButton btnViewTrips = createStyledButton("View Trips", new Color(0, 204, 255));
        gbc.gridy = 9;
        mainPanel.add(btnViewTrips, gbc);
        btnViewTrips.addActionListener(e -> viewTrips());
    }

    private JLabel createStyledLabel(String text, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, fontSize));
        label.setForeground(Color.WHITE); // White font for better contrast
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setOpaque(true);
        textField.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void addTrip() {
        try {
            int userId = Integer.parseInt(txtUserId.getText());
            String destination = destinationField.getText();
            Date startDate = Date.valueOf(startDateField.getText());
            Date endDate = Date.valueOf(endDateField.getText());

            boolean result = TripManagementMySQL.insertTrip(userId, destination, startDate, endDate);
            JOptionPane.showMessageDialog(mainPanel, result ? "Trip added successfully!" : "Failed to add trip.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + ex.getMessage());
        }
    }

    private void updateTrip() {
        try {
            int tripId = Integer.parseInt(tripIdField.getText());
            String destination = destinationField.getText();
            Date startDate = Date.valueOf(startDateField.getText());
            Date endDate = Date.valueOf(endDateField.getText());

            boolean result = TripManagementMySQL.updateTrip(tripId, destination, startDate, endDate);
            JOptionPane.showMessageDialog(mainPanel, result ? "Trip updated successfully!" : "Failed to update trip.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + ex.getMessage());
        }
    }

    private void deleteTrip() {
        try {
            int tripId = Integer.parseInt(tripIdField.getText());
            boolean result = TripManagementMySQL.deleteTrip(tripId);
            JOptionPane.showMessageDialog(mainPanel, result ? "Trip deleted successfully!" : "Failed to delete trip.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + ex.getMessage());
        }
    }

    private void viewTrips() {
        try {
            int userId = Integer.parseInt(txtUserId.getText());
            List<String[]> trips = TripManagementMySQL.viewTrips(userId);

            StringBuilder tripDetails = new StringBuilder("Your Trips:\n");
            for (String[] trip : trips) {
                tripDetails.append("Trip ID: ").append(trip[0])
                        .append(", Destination: ").append(trip[1])
                        .append(", Start Date: ").append(trip[2])
                        .append(", End Date: ").append(trip[3]).append("\n");
            }

            JOptionPane.showMessageDialog(mainPanel, tripDetails.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + ex.getMessage());
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}
