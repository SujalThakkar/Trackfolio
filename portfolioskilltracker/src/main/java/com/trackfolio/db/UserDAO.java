package com.trackfolio.db;

import com.trackfolio.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        this.conn = DBConnection.getConnection();
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("User registered: " + user.getUsername() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name")
                );
                SkillDAO skillDAO = new SkillDAO();
                CertificateDAO certDAO = new CertificateDAO();
                user.setSkills(skillDAO.getSkillsByUser(user.getUserId()));
                user.setCertificates(certDAO.getCertificatesByUser(user.getUserId()));
                System.out.println("User retrieved: " + username);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name")
                );
                SkillDAO skillDAO = new SkillDAO();
                CertificateDAO certDAO = new CertificateDAO();
                user.setSkills(skillDAO.getSkillsByUser(user.getUserId()));
                user.setCertificates(certDAO.getCertificatesByUser(user.getUserId()));
                System.out.println("User logged in: " + username);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error logging in user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}