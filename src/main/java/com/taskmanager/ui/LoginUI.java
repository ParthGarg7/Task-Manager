package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.imp1.UserDAOImpl;
import com.taskmanager.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private String loggedInUsername;
    private UserDAO userDAO = new UserDAOImpl();

    public LoginUI() {
        setTitle("Task Manager - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
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
        JPanel outerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 40));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);

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

        outerPanel.add(panel, gbc);

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
            new DashboardUI(username); // Open Dashboard with username
        });

        goToSignUpButton.addActionListener(e -> cardLayout.show(mainContainer, "SignUp"));

        return outerPanel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JTextField newUsernameField = new JTextField();
        newUsernameField.setMaximumSize(new Dimension(400, 40));

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setMaximumSize(new Dimension(400, 40));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new);
    }
}
