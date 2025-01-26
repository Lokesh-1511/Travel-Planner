package org.example.gui;

import org.example.mysql.UserAuthMySQL;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UserAuthGUI {
    private JPanel panel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image backgroundImage;

    public UserAuthGUI() {
        // Load background image
        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("center-image.jpg"))).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            backgroundImage = null; // Fallback if image fails to load
        }

        // Create custom JPanel with background image
        panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Create CardLayout to switch between login and signup
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false); // Transparent card panel to show background

        // Create login and signup panels
        JPanel loginPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();

        // Add both panels to the card panel
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(signupPanel, "Signup");

        // Add card panel to the main panel
        panel.add(cardPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setOpaque(true); // Ensure the panel is opaque so we can apply background color
        loginPanel.setBackground(new Color(0, 0, 0, 100)); // Slightly opaque black background (alpha=100)

        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField();
        JLabel passwordLabel = createStyledLabel("Password:");
        JPasswordField passwordField = createStyledPasswordField();

        JButton loginButton = createStyledButton("Login");
        JButton signupButton = createStyledButton("Signup");

        // Arrange components using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        loginPanel.add(signupButton, gbc);

        // Button actions
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup"));
        loginButton.addActionListener(e -> handleLogin(emailField.getText(), new String(passwordField.getPassword()), loginPanel));

        return loginPanel;
    }

    private JPanel createSignupPanel() {
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridBagLayout());
        signupPanel.setOpaque(true); // Ensure the panel is opaque so we can apply background color
        signupPanel.setBackground(new Color(0, 0, 0, 100)); // Slightly opaque black background (alpha=100)

        JLabel emailLabel = createStyledLabel("Email:");
        JTextField emailField = createStyledTextField();
        JLabel passwordLabel = createStyledLabel("Password:");
        JPasswordField passwordField = createStyledPasswordField();
        JLabel confirmPasswordLabel = createStyledLabel("Confirm Password:");
        JPasswordField confirmPasswordField = createStyledPasswordField();

        JButton signupButton = createStyledButton("Signup");
        JButton loginButton = createStyledButton("Login");

        // Arrange components using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        signupPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        signupPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        signupPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        signupPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        signupPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        signupPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        signupPanel.add(signupButton, gbc);

        gbc.gridy = 4;
        signupPanel.add(loginButton, gbc);

        // Button actions
        loginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        signupButton.addActionListener(e -> handleSignup(emailField.getText(), new String(passwordField.getPassword()), new String(confirmPasswordField.getPassword()), signupPanel));

        return signupPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE); // Changed label color to white
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        return passwordField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 204, 102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void handleLogin(String username, String password, JPanel loginPanel) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginPanel, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = UserAuthMySQL.login(username, password);
        if (userId != -1) {
            JOptionPane.showMessageDialog(loginPanel, "Login successful! Your User ID is: " + userId, "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(loginPanel, "Invalid email or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup(String username, String password, String confirmPassword, JPanel signupPanel) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(signupPanel, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(signupPanel, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = UserAuthMySQL.register(username, password);
        if (userId != -1) {
            JOptionPane.showMessageDialog(signupPanel, "Signup successful! Your User ID is: " + userId, "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(signupPanel, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
