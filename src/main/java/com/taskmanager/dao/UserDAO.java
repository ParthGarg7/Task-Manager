package com.taskmanager.dao;

import com.taskmanager.model.User;

public interface UserDAO {
    boolean isUserExists(String username);  // Check if a user exists
    boolean authenticateUser(String username, String passwordHash);  // Authenticate the user
    boolean registerUser(User user);  // Register a new user
    User getUserByUsername(String username);  // Get user details by username
    int getUserId(String username);  // Get user ID by username
}