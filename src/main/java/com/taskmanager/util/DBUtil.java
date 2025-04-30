package com.taskmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:taskmanager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table if it doesn't exist
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL
                );
            """;
            stmt.execute(createUsersTable);

            // ✅ Ensure 'password_hash' column exists (in case table already existed without it)
            try {
                stmt.execute("SELECT password_hash FROM users LIMIT 1;");
            } catch (SQLException e) {
                // If column does not exist, add it
                String alterTable = "ALTER TABLE users ADD COLUMN password_hash TEXT;";
                stmt.execute(alterTable);
                System.out.println("✅ Added missing column 'password_hash' to users table.");
            }

            // Create tasks table if it doesn't exist
            String createTasksTable = """
                CREATE TABLE IF NOT EXISTS tasks (
                    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    description TEXT,
                    due_date TEXT,
                    status TEXT DEFAULT 'Pending',
                    priority TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                );
            """;
            stmt.execute(createTasksTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
