package com.taskmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:taskmanager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    static {
        try (Connection conn = getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                                 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "username TEXT UNIQUE NOT NULL," +
                                 "password TEXT NOT NULL)";
            conn.createStatement().execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
