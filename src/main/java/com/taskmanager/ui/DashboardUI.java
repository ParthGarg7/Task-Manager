package com.taskmanager.ui;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.imp1.UserDAOImpl;
import com.taskmanager.model.Task;
import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class DashboardUI extends JFrame {
    private JLabel welcomeLabel;
    private String loggedInUsername;
    private UserDAO userDAO = new UserDAOImpl(); // Initialized here

    public DashboardUI(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername; // Assign to instance variable

        setTitle("Task Manager - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel dashboardPanel = createDashboardPanel(loggedInUsername);
        add(dashboardPanel);

        setVisible(true);
    }

    private JPanel createDashboardPanel(String username) {
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
        welcomeLabel = new JLabel("Welcome, " + username);
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
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});

        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        datePanel.add(dateSpinner);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(new JLabel("Task Title:"));
        form.add(titleField);
        form.add(Box.createVerticalStrut(5));
        form.add(new JLabel("Description:"));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        form.add(scrollPane);
        form.add(Box.createVerticalStrut(5));
        form.add(new JLabel("Due Date:"));
        form.add(datePanel);
        form.add(Box.createVerticalStrut(5));
        form.add(new JLabel("Priority:"));
        form.add(priorityBox);
        form.add(Box.createVerticalStrut(5));
        form.add(new JLabel("Status:"));
        form.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Create New Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String priority = (String) priorityBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Task title cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Task task = new Task();
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setStatus(status);

                Date date = (Date) dateSpinner.getValue();
                task.setDueDate(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());

                int userId = userDAO.getUserId(loggedInUsername); // FIXED!
                task.setUserId(userId);

                TaskDAO taskDAO = new TaskDAO();
                taskDAO.addTask(task);

                JOptionPane.showMessageDialog(this, "Task created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // refreshDashboardData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error creating task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void handleSidebarAction(String action) {
        if (action.equals("Logout")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginUI();
            }
        } else if (action.equals("Help")) {
            new HelpWindow().setVisible(true); // Create HelpWindow.java separately
        } else {
            JOptionPane.showMessageDialog(this, action + " clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
