package org.example;

import org.example.gui.TripManagementGUI;
import org.example.gui.BudgetTrackerGUI;
import org.example.gui.UserAuthGUI;
import org.example.gui.ItineraryManagementGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Main {
    private JFrame frame;
    private JPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        frame = new JFrame("Travel Planner Application");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with CardLayout
        mainPanel = new JPanel(new CardLayout());

        // Adding panels for different modules
        mainPanel.add(new UserAuthGUI().getPanel(), "Login");
        mainPanel.add(new BudgetTrackerGUI().getPanel(), "Budget Tracker");
        mainPanel.add(new TripManagementGUI().getPanel(), "Trip Management");

        // Creating an instance of ItineraryManagementGUI and adding it to main panel
        ItineraryManagementGUI itineraryManagementGUI = new ItineraryManagementGUI();
        mainPanel.add(itineraryManagementGUI.createPanel(), "Itinerary Management");

        // Create a navigation bar
        JPanel navBar = new JPanel();
        navBar.setLayout(new FlowLayout(FlowLayout.LEFT)); // Buttons aligned to the left
        navBar.setBackground(Color.LIGHT_GRAY);

        // Add navigation buttons
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> switchToPanel("Login"));
        navBar.add(btnLogin);

        JButton btnBudgetTracker = new JButton("Budget Tracker");
        btnBudgetTracker.addActionListener(e -> switchToPanel("Budget Tracker"));
        navBar.add(btnBudgetTracker);

        JButton btnTripManagement = new JButton("Trip Management");
        btnTripManagement.addActionListener(e -> switchToPanel("Trip Management"));
        navBar.add(btnTripManagement);

        JButton btnItineraryManagement = new JButton("Itinerary Management");
        btnItineraryManagement.addActionListener(e -> switchToPanel("Itinerary Management"));
        navBar.add(btnItineraryManagement);

        // Add navigation bar and main panel to frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(navBar, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Add a fullscreen background image
        addFullscreenImage();

        frame.setVisible(true);
    }

    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelName);
    }

    private void addFullscreenImage() {
        // Create a JPanel with custom painting
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Use the ClassLoader to get the resource
                    Image image = javax.imageio.ImageIO.read(
                            Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("center-image.jpg"))
                    );
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Stretched to fit the panel
                } catch (Exception e) {
                    e.printStackTrace();
                    g.setColor(Color.RED);
                    g.drawString("Failed to load image", 10, 20);
                }
            }
        };

        imagePanel.setLayout(new BorderLayout()); // Allows adding other components if needed
        mainPanel.add(imagePanel, "Home");

        // Show image by default
        switchToPanel("Home");

        // Force repaint to ensure new image is displayed
        mainPanel.repaint();
    }


}
