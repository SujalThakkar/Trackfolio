package com.trackfolio.model;

import java.time.LocalDate;

public class RatingHistory {
    private int historyId;
    private int skillId;
    private int rating;
    private LocalDate updateDate;

    public RatingHistory(int skillId, int rating, LocalDate updateDate) {
        this.skillId = skillId;
        this.rating = rating;
        this.updateDate = updateDate;
    }

    public RatingHistory(int historyId, int skillId, int rating, LocalDate updateDate) {
        this.historyId = historyId;
        this.skillId = skillId;
        this.rating = rating;
        this.updateDate = updateDate;
    }

    // Getters and Setters
    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }
    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public LocalDate getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDate updateDate) { this.updateDate = updateDate; }
}
