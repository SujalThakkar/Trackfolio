package com.trackfolio.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = "jdbc:sqlite:trackfolio.db";
                conn = DriverManager.getConnection(url);
                System.out.println("Database connection established");
                initializeDatabase();
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }

    private static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "full_name TEXT NOT NULL)";
        String createSkillsTable = "CREATE TABLE IF NOT EXISTS skills (" +
                "skill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "skill_name TEXT NOT NULL, " +
                "level TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";
        String createCertificatesTable = "CREATE TABLE IF NOT EXISTS certificates (" +
                "cert_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "cert_name TEXT NOT NULL, " +
                "issued_by TEXT NOT NULL, " +
                "issue_date TEXT NOT NULL, " +
                "file_path TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createSkillsTable);
            stmt.execute(createCertificatesTable);
            System.out.println("Database tables initialized");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}