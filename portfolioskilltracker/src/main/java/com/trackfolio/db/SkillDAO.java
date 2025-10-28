package com.trackfolio.db;

import com.trackfolio.model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {
    private Connection conn;

    public SkillDAO() {
        this.conn = DBConnection.getConnection();
    }

    public int addSkill(Skill skill) {
        String sql = "INSERT INTO skills (user_id, skill_name, level, is_coding, rating) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, skill.getUserId());
            pstmt.setString(2, skill.getSkillName());
            pstmt.setString(3, skill.getLevel());
            pstmt.setBoolean(4, skill.isCoding());
            if (skill.getRating() != null) {
                pstmt.setInt(5, skill.getRating());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Skill added to database, rows affected: " + rowsAffected);
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("Error adding skill: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<Skill> getSkillsByUser(int userId) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Skill skill = new Skill(
                        rs.getInt("skill_id"),
                        rs.getInt("user_id"),
                        rs.getString("skill_name"),
                        rs.getString("level"),
                        rs.getBoolean("is_coding"),
                        rs.getObject("rating") != null ? rs.getInt("rating") : null
                );
                skills.add(skill);
            }
            System.out.println("Retrieved " + skills.size() + " skills for user_id: " + userId);
        } catch (SQLException e) {
            System.err.println("Error retrieving skills: " + e.getMessage());
            e.printStackTrace();
        }
        return skills;
    }

    public boolean deleteSkill(int skillId) {
        String sql = "DELETE FROM skills WHERE skill_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, skillId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Skill deleted, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting skill: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRating(int skillId, int newRating) {
        String sql = "UPDATE skills SET rating = ? WHERE skill_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newRating);
            pstmt.setInt(2, skillId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rating updated, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating rating: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}