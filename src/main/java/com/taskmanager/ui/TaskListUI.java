package com.taskmanager.ui;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskListUI extends JFrame {
    private final int userId;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private TaskDAO taskDAO;
    private final DashboardUI dashboardUI;

    public TaskListUI(int userId, DashboardUI dashboardUI) {
        this.userId = userId;
        this.dashboardUI = dashboardUI;
        this.taskDAO = new TaskDAO();
        
        setTitle("Task Manager - Task List");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        loadTasks();
        
        setVisible(true);
    }
    private void loadTasks()
    {
        // ✅ NEW: Update overdue tasks before loading
        taskDAO.updateOverdueTasks(userId);

        tableModel.setRowCount(0);
        List<Task> tasks = taskDAO.getAllTasksByUser(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (Task task : tasks) {
            Object[] rowData =
                    {
                            task.getTaskId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getDueDate() != null ? task.getDueDate().format(formatter) : "No Date",
                            task.getPriority(),
                            task.getStatus(),
                            task.getCreatedAt()
                    };
            tableModel.addRow(rowData);
        }
    }

    private void initComponents() {
        // Table setup
        String[] columnNames = {"ID", "Title", "Description", "Due Date", "Priority", "Status", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setRowHeight(30);
        
        // Add double-click listener to edit task
        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = taskTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
                        Task task = taskDAO.getTaskById(taskId);
                        openTaskEditDialog(task);
                    }
                }
            }
        });
        
        // Action buttons
        JPanel buttonPanel = new JPanel();
        JButton changeStatusButton = new JButton("Change Status");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        
        changeStatusButton.addActionListener(e -> changeTaskStatus());
        editButton.addActionListener(e -> editSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        refreshButton.addActionListener(e -> loadTasks());
        
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Pending", "In Progress", "Completed"});
        JLabel filterLabel = new JLabel("Filter by Status: ");
        
        statusFilter.addActionListener(e -> {
            String selectedStatus = (String) statusFilter.getSelectedItem();
            filterTasksByStatus(selectedStatus);
        });
        
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);
        
        // Main layout
        setLayout(new BorderLayout());
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(filterPanel, BorderLayout.NORTH);
    }
    
    
    private void filterTasksByStatus(String status) {
        // Clear existing table data
        tableModel.setRowCount(0);
        
        // Get filtered tasks
        List<Task> tasks;
        if (status.equals("All")) {
            tasks = taskDAO.getAllTasksByUser(userId);
        } else {
            tasks = taskDAO.getTasksByStatus(userId, status);
        }
        
        // Populate table with filtered tasks
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        for (Task task : tasks) {
            Object[] rowData = {
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate() != null ? task.getDueDate().format(formatter) : "No Date",
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt()
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void changeTaskStatus() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
        Task task = taskDAO.getTaskById(taskId);
        
        if (task != null) {
            String[] statuses = {"Pending", "In Progress", "Completed"};
            String currentStatus = task.getStatus();
            
            // Show dialog with status options
            String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status for task: " + task.getTitle(),
                "Change Task Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                currentStatus
            );
            
            if (newStatus != null && !newStatus.equals(currentStatus)) {
                task.setStatus(newStatus);
                taskDAO.updateTask(task);
                loadTasks();
                JOptionPane.showMessageDialog(this, "Task status updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update dashboard status counts
                if (dashboardUI != null) {
                    dashboardUI.updateStatusCounts();
                }
            }
        }
    }
    
    private void editSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
        Task task = taskDAO.getTaskById(taskId);
        
        if (task != null) {
            openTaskEditDialog(task);
        }
    }
    
    private void openTaskEditDialog(Task task) {
        JTextField titleField = new JTextField(task.getTitle());
        JTextArea descriptionArea = new JTextArea(task.getDescription(), 4, 20);
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        priorityBox.setSelectedItem(task.getPriority());
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        statusBox.setSelectedItem(task.getStatus());
        
        // Date picker for due date
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        
        // Set current due date if available
        if (task.getDueDate() != null) {
            java.util.Date date = java.sql.Date.valueOf(task.getDueDate());
            dateSpinner.setValue(date);
        }
        
        datePanel.add(dateSpinner);
        
        // Create form panel
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
        
        // Show edit dialog
        int result = JOptionPane.showConfirmDialog(this, form, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Update task with new values
            task.setTitle(titleField.getText().trim());
            task.setDescription(descriptionArea.getText().trim());
            task.setPriority((String) priorityBox.getSelectedItem());
            task.setStatus((String) statusBox.getSelectedItem());
            
            // Update due date
            java.util.Date date = (java.util.Date) dateSpinner.getValue();
            task.setDueDate(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            
            // Save updates to database
            taskDAO.updateTask(task);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Update dashboard status counts
            if (dashboardUI != null) {
                dashboardUI.updateStatusCounts();
            }
        }
    }
    
    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this task?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            taskDAO.deleteTask(taskId);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Update dashboard status counts
            if (dashboardUI != null) {
                dashboardUI.updateStatusCounts();
            }
        }
    }
}