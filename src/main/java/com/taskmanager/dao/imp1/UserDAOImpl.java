package com.taskmanager.dao.imp1;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.User;
import com.taskmanager.util.DBUtil;
import java.sql.*;

public class UserDAOImpl implements UserDAO {

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
            String query = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password").equals(password);  // Check if passwords match
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
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                int rowsAffected = stmt.executeUpdate();  // Execute insert query
                return rowsAffected > 0;  // Return true if a row was inserted (i.e., user registered)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Return false if there was an error
    }
}
