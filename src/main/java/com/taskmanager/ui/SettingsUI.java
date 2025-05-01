package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.dao.impl.UserDAOImpl;
import com.taskmanager.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SettingsUI extends JFrame {
    private final int userId;
    private final String username;
    private final UserDAO userDAO;
    private final DashboardUI dashboardUI;
    
    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(SettingsUI.class);

    public SettingsUI(int userId, String username, DashboardUI dashboardUI) {
        this.userId = userId;
        this.username = username;
        this.dashboardUI = dashboardUI;
        this.userDAO = new UserDAOImpl();
        
        setTitle("Task Manager - Settings");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        // Add window listener to update dashboard's active panel status when this window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // When this window is closed, set Dashboard as active panel again
                if (dashboardUI != null) {
                    dashboardUI.updateActivePanel("Dashboard");
                }
            }
        });
        
        setVisible(true);
    }

    private void initComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // User info display
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        
        JLabel usernameLabel = new JLabel("Username: " + username);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createVerticalStrut(10));
        
        // Settings options panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings Options"));
        
        // Change password button
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        changePasswordButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePasswordButton.setMaximumSize(new Dimension(200, 40));
        changePasswordButton.addActionListener(e -> openChangePasswordDialog());
        
        // Theme settings (placeholder for future implementation)
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        themePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel themeLabel = new JLabel("Theme: ");
        themeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String[] themes = {"Light", "Dark", "System Default"};
        JComboBox<String> themeSelector = new JComboBox<>(themes);
        themeSelector.setEnabled(false); // Disabled for now
        themePanel.add(themeLabel);
        themePanel.add(themeSelector);
        
        // Add components to settings panel
        settingsPanel.add(changePasswordButton);
        settingsPanel.add(Box.createVerticalStrut(15));
        settingsPanel.add(themePanel);
        
        // Notification settings (placeholder for future implementation)
        JPanel notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBorder(BorderFactory.createTitledBorder("Notification Settings"));
        notificationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox enableNotificationsCheck = new JCheckBox("Enable Task Due Date Notifications");
        enableNotificationsCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        enableNotificationsCheck.setEnabled(false); // Disabled for now
        
        JCheckBox enableSoundCheck = new JCheckBox("Enable Notification Sounds");
        enableSoundCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        enableSoundCheck.setEnabled(false); // Disabled for now
        
        notificationPanel.add(enableNotificationsCheck);
        notificationPanel.add(enableSoundCheck);
        
        // Note about disabled features
        JLabel noteLabel = new JLabel("<html><i>Note: Some settings features are still under development.</i></html>");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        noteLabel.setForeground(Color.GRAY);
        
        // Add all panels to main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(userInfoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(settingsPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(notificationPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(noteLabel);
        
        // Add to main layout
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void openChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        panel.add(new JLabel("Current Password:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validate current password
            if (!userDAO.authenticateUser(username, currentPassword)) {
                JOptionPane.showMessageDialog(this, 
                        "Current password is incorrect.", 
                        "Authentication Failed", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate new password
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, 
                        "New password must be at least 6 characters long.", 
                        "Invalid Password", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate new passwords match
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                        "New passwords do not match.", 
                        "Password Mismatch", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Update password in database
            try {
                User user = new User(userId, username, newPassword);
                userDAO.updateUserPassword(user);
                JOptionPane.showMessageDialog(this, 
                        "Password updated successfully.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Log the error using SLF4J
                logger.error("Failed to update password for user: {}", username, e);
                JOptionPane.showMessageDialog(this, 
                        "Failed to update password: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
