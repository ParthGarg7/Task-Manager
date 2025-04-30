package com.taskmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class TaskCalendarPanel extends JPanel {
    private YearMonth currentMonth;
    private Map<LocalDate, List<String>> tasksByDate; // Map of date -> list of tasks

    public TaskCalendarPanel(Map<LocalDate, List<String>> tasksByDate) {
        this.currentMonth = YearMonth.now();
        this.tasksByDate = tasksByDate;
        setLayout(new GridLayout(0, 7, 10, 10)); // 7 days a week
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        drawCalendar();
    }

    private void drawCalendar() {
        removeAll();
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday ... 7=Sunday

        // Add day headers
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            add(label);
        }

        // Fill blanks before the first day
        for (int i = 1; i < dayOfWeek; i++) {
            add(new JLabel(""));
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            add(createDayPanel(date));
        }

        revalidate();
        repaint();
    }

    private JPanel createDayPanel(LocalDate date) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 240, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(dayLabel, BorderLayout.NORTH);

        // Highlight current day
        if (date.equals(LocalDate.now())) {
            panel.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));
        }

        List<String> tasks = tasksByDate.get(date);
        if (tasks != null && !tasks.isEmpty()) {
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
            taskPanel.setOpaque(false);
            int maxTasks = 3;
            for (int i = 0; i < Math.min(tasks.size(), maxTasks); i++) {
                JLabel taskLabel = new JLabel("• " + tasks.get(i));
                taskLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                taskPanel.add(taskLabel);
            }
            panel.add(taskPanel, BorderLayout.CENTER);
        }

        return panel;
    }

    public void updateTasks(Map<LocalDate, List<String>> updatedTasks) {
        this.tasksByDate = updatedTasks;
        drawCalendar();
    }
} 
