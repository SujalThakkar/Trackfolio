package com.trackfolio.db;

import com.trackfolio.model.RatingHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RatingHistoryDAO {
    private Connection conn;

    public RatingHistoryDAO() {
        this.conn = DBConnection.getConnection();
    }

    public boolean addRatingHistory(RatingHistory history) {
        String sql = "INSERT INTO rating_history (skill_id, rating, update_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, history.getSkillId());
            pstmt.setInt(2, history.getRating());
            pstmt.setString(3, history.getUpdateDate().toString());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rating history added, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding rating history: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<RatingHistory> getRatingHistoryBySkill(int skillId) {
        List<RatingHistory> history = new ArrayList<>();
        String sql = "SELECT * FROM rating_history WHERE skill_id = ? ORDER BY update_date ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, skillId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RatingHistory rh = new RatingHistory(
                        rs.getInt("history_id"),
                        rs.getInt("skill_id"),
                        rs.getInt("rating"),
                        LocalDate.parse(rs.getString("update_date"))
                );
                history.add(rh);
            }
            System.out.println("Retrieved " + history.size() + " rating history entries for skill_id: " + skillId);
        } catch (SQLException e) {
            System.err.println("Error retrieving rating history: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }
}
