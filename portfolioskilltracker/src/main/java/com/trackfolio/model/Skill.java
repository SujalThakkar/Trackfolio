package com.trackfolio.model;

public class Skill {
    private int skillId;
    private int userId;
    private String skillName;
    private String level;
    private boolean isCoding;
    private Integer rating; // Nullable for non-coding skills

    public Skill(int userId, String skillName, String level, boolean isCoding, Integer rating) {
        this.userId = userId;
        this.skillName = skillName;
        this.level = level;
        this.isCoding = isCoding;
        this.rating = rating;
    }

    public Skill(int skillId, int userId, String skillName, String level, boolean isCoding, Integer rating) {
        this.skillId = skillId;
        this.userId = userId;
        this.skillName = skillName;
        this.level = level;
        this.isCoding = isCoding;
        this.rating = rating;
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
    public boolean isCoding() { return isCoding; }
    public void setCoding(boolean coding) { isCoding = coding; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    @Override
    public String toString() {
        return "Skill{id=" + skillId + ", name=" + skillName + ", level=" + level + ", isCoding=" + isCoding + ", rating=" + rating + "}";
    }
}