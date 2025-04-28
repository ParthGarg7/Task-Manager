package com.taskmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:task_manager.db"; // local file-based DB

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
