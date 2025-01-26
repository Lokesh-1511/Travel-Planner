package org.example.gui;

import org.example.mysql.ItineraryManagementMySQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class ItineraryManagementGUI {
    private JTextArea itineraryDisplay;
    private JTextField activityField;
    private JTextField userIdField;
    private JTextField dateField;
    private JTextField tripIdField;
    private Image backgroundImage;

    public JPanel createPanel() {
        // Load background image
        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("center-image.jpg"))
            ).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            backgroundImage = null; // Fallback if image fails to load
        }

        // Custom JPanel to display the background image
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setOpaque(false); // Ensure the panel is transparent for the background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing for components

        // Input panel with semi-transparent background
        JPanel inputPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f)); // Set transparency level
                g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        inputPanel.setOpaque(false); // Transparent background for the input panel

        // Labels and text fields
        JLabel userIdLabel = createStyledLabel("User ID:");
        JLabel tripIdLabel = createStyledLabel("Trip ID:");
        JLabel dateLabel = createStyledLabel("Date (yyyy-mm-dd):");
        JLabel activityLabel = createStyledLabel("Activity:");

        userIdField = createStyledTextField();
        tripIdField = createStyledTextField();
        dateField = createStyledTextField();
        activityField = createStyledTextField();

        // Buttons
        JButton addButton = createStyledButton("Add Activity", new Color(66, 133, 244));
        JButton loadButton = createStyledButton("Load Itinerary", new Color(34, 153, 84));

        // Layout for input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(tripIdLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(tripIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(activityLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(activityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(addButton, gbc);

        gbc.gridx = 1;
        inputPanel.add(loadButton, gbc);

        // Display area for itinerary
        itineraryDisplay = new JTextArea(10, 40);
        itineraryDisplay.setEditable(false);
        itineraryDisplay.setFont(new Font("Arial", Font.PLAIN, 14));
        itineraryDisplay.setLineWrap(true);
        itineraryDisplay.setWrapStyleWord(true);
        itineraryDisplay.setBackground(Color.WHITE); // Set itinerary box background to white
        itineraryDisplay.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add a border

        JScrollPane scrollPane = new JScrollPane(itineraryDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        scrollPane.setOpaque(false); // Transparent scroll pane border
        scrollPane.getViewport().setOpaque(true); // Ensure viewport remains white for the JTextArea

        // Add components to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> handleAddAction(panel));
        loadButton.addActionListener(e -> handleLoadAction(panel));

        return panel;
    }

    private void handleAddAction(JPanel panel) {
        String activity = activityField.getText();
        String userIdText = userIdField.getText();
        String tripIdText = tripIdField.getText();
        String dateText = dateField.getText();

        if (!activity.isEmpty() && !userIdText.isEmpty() && !tripIdText.isEmpty() && !dateText.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdText);
                int tripId = Integer.parseInt(tripIdText);
                Date date = Date.valueOf(dateText);
                boolean success = ItineraryManagementMySQL.addItinerary(userId, tripId, date, activity);
                JOptionPane.showMessageDialog(panel, success ? "Activity added successfully!" : "Failed to add activity.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Invalid input: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please fill all fields.");
        }
    }

    private void handleLoadAction(JPanel panel) {
        String userIdText = userIdField.getText();
        String tripIdText = tripIdField.getText();

        if (!userIdText.isEmpty() && !tripIdText.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdText);
                int tripId = Integer.parseInt(tripIdText);
                List<String> itinerary = ItineraryManagementMySQL.getItineraryForTrip(userId, tripId);
                itineraryDisplay.setText(""); // Clear previous results
                if (itinerary.isEmpty()) {
                    itineraryDisplay.append("No activities found for Trip ID: " + tripId);
                } else {
                    for (String activity : itinerary) {
                        itineraryDisplay.append(activity + "\n");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please enter both user ID and trip ID.");
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE); // White text for better contrast
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return textField;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }
}
