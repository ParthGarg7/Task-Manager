package com.taskmanager.dao;

import com.taskmanager.model.Task;
import com.taskmanager.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public TaskDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "due_date TEXT," +
                "priority TEXT," +
                "status TEXT" +
                ");";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, due_date, priority, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                String dueDateStr = rs.getString("due_date");
                if (dueDateStr != null) {
                    task.setDueDate(LocalDate.parse(dueDateStr));
                }
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, priority = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());
            pstmt.setInt(6, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
