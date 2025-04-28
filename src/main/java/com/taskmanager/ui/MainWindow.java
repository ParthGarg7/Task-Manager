package com.taskmanager.ui;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Task Manager App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window

        // Later we'll add buttons, task lists here
    }
}
