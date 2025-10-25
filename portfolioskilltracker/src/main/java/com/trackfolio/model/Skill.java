package com.trackfolio.model;

public class Skill {
    private int skillId;
    private int userId;
    private String skillName;
    private String level;

    public Skill(int userId, String skillName, String level) {
        this.userId = userId;
        this.skillName = skillName;
        this.level = level;
    }

    public Skill(int skillId, int userId, String skillName, String level) {
        this.skillId = skillId;
        this.userId = userId;
        this.skillName = skillName;
        this.level = level;
    }

    // Getters and Setters
    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    @Override
    public String toString() {
        return "Skill{id=" + skillId + ", name=" + skillName + ", level=" + level + "}";
    }
}