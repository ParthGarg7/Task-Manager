package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.imp1.UserDAOImpl;
import com.taskmanager.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

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

            loggedInUsername = username;
            welcomeLabel.setText("Welcome, " + loggedInUsername);
            cardLayout.show(mainContainer, "Dashboard");
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

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(new Color(45, 45, 45));

        String[] navItems = {"Dashboard", "Tasks", "Categories", "Settings", "Help", "Logout"};
        for (String item : navItems) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            sidebar.add(Box.createVerticalStrut(15));
            sidebar.add(btn);
            btn.addActionListener(e -> handleSidebarAction(item));
        }

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel welcomeSection = new JPanel();
        welcomeSection.setLayout(new BoxLayout(welcomeSection, BoxLayout.Y_AXIS));
        welcomeSection.setBackground(new Color(240, 240, 240));
        welcomeSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        welcomeSection.add(new JLabel("👋 Welcome to your Task Dashboard"));
        welcomeSection.add(Box.createVerticalStrut(5));
        welcomeSection.add(new JSeparator(SwingConstants.HORIZONTAL));

        JButton createTaskButton = new JButton("➕ Create New Task");
        createTaskButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createTaskButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createTaskButton.setBackground(new Color(76, 175, 80));
        createTaskButton.setForeground(Color.WHITE);
        createTaskButton.setFocusPainted(false);
        createTaskButton.addActionListener(e -> openTaskCreationForm());

        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statusPanel.setMaximumSize(new Dimension(1000, 80));
        statusPanel.add(createStatusBox("Completed", "12", new Color(76, 175, 80)));
        statusPanel.add(createStatusBox("In Progress", "5", new Color(255, 193, 7)));
        statusPanel.add(createStatusBox("Pending", "8", new Color(244, 67, 54)));

        JPanel recentActivities = new JPanel();
        recentActivities.setLayout(new BoxLayout(recentActivities, BoxLayout.Y_AXIS));
        recentActivities.setBorder(BorderFactory.createTitledBorder("🕒 Recent Activities"));
        recentActivities.add(new JLabel("• Task 'Finish Report' created"));
        recentActivities.add(new JLabel("• Task 'Buy Groceries' updated"));

        JPanel recentCompleted = new JPanel();
        recentCompleted.setLayout(new BoxLayout(recentCompleted, BoxLayout.Y_AXIS));
        recentCompleted.setBorder(BorderFactory.createTitledBorder("✅ Recently Completed"));
        recentCompleted.add(new JLabel("• 'Submit Assignment' - Apr 26"));
        recentCompleted.add(new JLabel("• 'Team Meeting' - Apr 25"));

        contentPanel.add(welcomeSection);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(createTaskButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(statusPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(recentActivities);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(recentCompleted);

        dashboardPanel.add(sidebar, BorderLayout.WEST);
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        dashboardPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        return dashboardPanel;
    }

    private JPanel createStatusBox(String title, String value, Color color) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(color);
        box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelTitle = new JLabel(title, SwingConstants.CENTER);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelTitle.setForeground(Color.WHITE);

        JLabel labelValue = new JLabel(value, SwingConstants.CENTER);
        labelValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelValue.setForeground(Color.WHITE);

        box.add(labelTitle, BorderLayout.NORTH);
        box.add(labelValue, BorderLayout.CENTER);

        return box;
    }

    private void openTaskCreationForm() {
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(4, 20);
        JTextField tagsField = new JTextField();
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JSpinner dueDateSpinner = new JSpinner(new SpinnerDateModel());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(new JLabel("Task Title:"));
        form.add(titleField);
        form.add(new JLabel("Description:"));
        form.add(new JScrollPane(descriptionArea));
        form.add(new JLabel("Tags:"));
        form.add(tagsField);
        form.add(new JLabel("Due Date:"));
        form.add(dueDateSpinner);
        form.add(new JLabel("Priority:"));
        form.add(priorityBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Create New Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "Task created successfully!");
        }
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
