package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.imp1.UserDAOImpl;
import com.taskmanager.model.User;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private UserDAO userDAO = new UserDAOImpl();
    private BufferedImage backgroundImage;

    public LoginUI() {
        try {

            // Load the background image
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/BGimage1.jpg"));
            if (backgroundImage != null) {
                System.out.println("Image loaded successfully!");
            } else {
                System.out.println("Failed to load image.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createLoginPanel(), "Login");
        mainContainer.add(createSignUpPanel(), "SignUp");

        add(mainContainer);
        cardLayout.show(mainContainer, "Login");

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        BackgroundPanel outerPanel = new BackgroundPanel(backgroundImage);
        outerPanel.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Login to Task Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameField.setPreferredSize(new Dimension(300, 40));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setPreferredSize(new Dimension(300, 40));

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = createStyledButton("Login", new Color(33, 150, 243));
        JButton goToSignUpButton = createStyledButton("Sign Up", Color.WHITE);
        goToSignUpButton.setForeground(Color.BLACK);

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(goToSignUpButton);

        outerPanel.add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!userDAO.isUserExists(username)) {
                JOptionPane.showMessageDialog(null, "No such user found. Please sign up first.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userDAO.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(null, "Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.dispose(); // Close login window
            new DashboardUI(username); // Open dashboard
        });

        goToSignUpButton.addActionListener(e -> cardLayout.show(mainContainer, "SignUp"));
        return outerPanel;
    }

    private JPanel createSignUpPanel() {
        BackgroundPanel outerPanel = new BackgroundPanel(backgroundImage);
        outerPanel.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Sign Up for Task Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel newUserLabel = new JLabel("New Username:");
        newUserLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField newUsernameField = new JTextField();
        newUsernameField.setMaximumSize(new Dimension(400, 45));
        newUsernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newUsernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newUsernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setMaximumSize(new Dimension(400, 45));
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPasswordField.setBorder(newUsernameField.getBorder());

        JButton signUpButton = createStyledButton("Sign Up", new Color(76, 175, 80));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton goToLoginButton = createStyledButton("Back to Login", Color.WHITE);
        goToLoginButton.setForeground(Color.BLACK);
        goToLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(newUserLabel);
        panel.add(newUsernameField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(newPassLabel);
        panel.add(newPasswordField);
        panel.add(Box.createVerticalStrut(30));
        panel.add(signUpButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(goToLoginButton);

        outerPanel.add(panel);

        signUpButton.addActionListener(e -> {
            String username = newUsernameField.getText().trim();
            String password = new String(newPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (password.length() < 6) {
                JOptionPane.showMessageDialog(null, "Password must be at least 6 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (userDAO.isUserExists(username)) {
                JOptionPane.showMessageDialog(null, "User already exists. Please login.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                userDAO.registerUser(new User(username, password));
                JOptionPane.showMessageDialog(null, "Sign up successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainContainer, "Login");
            }
        });

        goToLoginButton.addActionListener(e -> cardLayout.show(mainContainer, "Login"));
        return outerPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private static class BackgroundPanel extends JPanel {

        private final BufferedImage image;

        public BackgroundPanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // 60% translucent
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}

