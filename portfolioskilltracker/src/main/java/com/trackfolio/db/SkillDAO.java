package com.trackfolio.db;

import com.trackfolio.model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {
    private Connection conn;

    public SkillDAO() {
        this.conn = DBConnection.getConnection();
    }

    public boolean addSkill(Skill skill) {
        String sql = "INSERT INTO skills (user_id, skill_name, level) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, skill.getUserId());
            pstmt.setString(2, skill.getSkillName());
            pstmt.setString(3, skill.getLevel());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Skill added to database, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding skill: " + e.getMessage());
            e.printStackTrace();
            return false;
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
                        rs.getString("level")
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
}