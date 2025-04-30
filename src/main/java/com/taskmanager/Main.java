package com.taskmanager;

import com.taskmanager.ui.LoginUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginUI(); // Launches the login/sign-up interface
        });
    }
}
