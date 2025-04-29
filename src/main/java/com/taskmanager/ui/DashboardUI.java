package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.imp1.UserDAOImpl;

import com.taskmanager.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JLabel welcomeLabel;
    private String loggedInUsername;
    private UserDAO userDAO = new UserDAOImpl();

    public DashboardUI() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createLoginPanel(), "Login");
        mainContainer.add(createSignUpPanel(), "SignUp");
        mainContainer.add(createDashboardPanel(), "Dashboard");

        add(mainContainer);
        cardLayout.show(mainContainer, "Login");

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 30));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));

        JButton loginButton = new JButton("Login");
        JButton goToSignUpButton = new JButton("Sign Up");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(goToSignUpButton);

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

            loggedInUsername = username;
            welcomeLabel.setText("Welcome, " + loggedInUsername);
            cardLayout.show(mainContainer, "Dashboard");
        });

        goToSignUpButton.addActionListener(e -> cardLayout.show(mainContainer, "SignUp"));

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JTextField newUsernameField = new JTextField();
        newUsernameField.setMaximumSize(new Dimension(300, 30));

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setMaximumSize(new Dimension(300, 30));

        JButton signUpButton = new JButton("Sign Up");
        JButton goToLoginButton = new JButton("Back to Login");

        panel.add(new JLabel("New Username:"));
        panel.add(newUsernameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(signUpButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(goToLoginButton);

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

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBackground(new Color(255, 87, 87));

        String[] buttons = {"Dashboard", "Vital Task", "My Task", "Task Categories", "Settings", "Help", "Logout"};
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(btn);

            btn.addActionListener(e -> handleSidebarAction(text));
        }

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Content Area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(new JLabel("Dashboard content will be displayed here."));

        dashboardPanel.add(sidebar, BorderLayout.WEST);
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        return dashboardPanel;
    }

    private void handleSidebarAction(String action) {
        if (action.equals("Logout")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainContainer, "Login");
            }
        } else {
            JOptionPane.showMessageDialog(this, action + " clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardUI::new);
    }
}
