package com.trackfolio;

import com.trackfolio.db.DBConnection;
import com.trackfolio.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI uses system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }

        // Initialize database connection (auto-create DB and tables)
        DBConnection.getConnection();

        // Launch Login GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(); // your Swing login page
            loginFrame.setVisible(true);
        });
    }
}