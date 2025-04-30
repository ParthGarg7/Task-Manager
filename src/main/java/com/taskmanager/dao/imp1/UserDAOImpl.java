package com.taskmanager.dao.imp1;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.User;
import com.taskmanager.util.DBUtil;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAOImpl implements UserDAO {

    // Helper method to hash passwords
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Should handle this better in production
        }
    }

    // Check if a user exists in the database by username
    @Override
    public boolean isUserExists(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT 1 FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();  // Returns true if user exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if there's an error
    }

    // Authenticate a user by comparing username and password
    @Override
    public boolean authenticateUser(String username, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT password_hash FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password_hash");
                        String inputHash = hashPassword(password);
                        return storedHash.equals(inputHash);  // Check if password hashes match
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if authentication fails
    }

    // Register a new user in the database
    @Override
    public boolean registerUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, hashPassword(user.getPasswordHash()));
                int rowsAffected = stmt.executeUpdate();  // Execute insert query
                return rowsAffected > 0;  // Return true if a row was inserted (i.e., user registered)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if there was an error
    }
    
    // Get user details by username
    @Override
    public User getUserByUsername(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT user_id, username, password_hash FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPasswordHash(rs.getString("password_hash"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get user ID by username
    @Override
    public int getUserId(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT user_id FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("user_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if user not found or an error occurred
    }
}