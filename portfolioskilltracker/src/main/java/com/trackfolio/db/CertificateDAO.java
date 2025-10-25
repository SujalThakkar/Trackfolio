package com.trackfolio.db;

import com.trackfolio.model.Certificate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAO {
    private Connection conn;

    public CertificateDAO() {
        this.conn = DBConnection.getConnection();
    }

    public boolean addCertificate(Certificate certificate) {
        String sql = "INSERT INTO certificates (user_id, cert_name, issued_by, issue_date, file_path) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, certificate.getUserId());
            pstmt.setString(2, certificate.getCertName());
            pstmt.setString(3, certificate.getIssuedBy());
            pstmt.setString(4, certificate.getIssueDate().toString());
            pstmt.setString(5, certificate.getFilePath());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Certificate added to database, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding certificate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Certificate> getCertificatesByUser(int userId) {
        List<Certificate> certificates = new ArrayList<>();
        String sql = "SELECT * FROM certificates WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Certificate cert = new Certificate(
                        rs.getInt("cert_id"),
                        rs.getInt("user_id"),
                        rs.getString("cert_name"),
                        rs.getString("issued_by"),
                        LocalDate.parse(rs.getString("issue_date")),
                        rs.getString("file_path")
                );
                certificates.add(cert);
            }
            System.out.println("Retrieved " + certificates.size() + " certificates for user_id: " + userId);
        } catch (SQLException e) {
            System.err.println("Error retrieving certificates: " + e.getMessage());
            e.printStackTrace();
        }
        return certificates;
    }

    public boolean deleteCertificate(int certId) {
        String sql = "DELETE FROM certificates WHERE cert_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, certId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Certificate deleted, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting certificate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}