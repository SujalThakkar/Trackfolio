package com.trackfolio.model;

import java.time.LocalDate;

public class Certificate {
    private int certId;
    private int userId;
    private String certName;
    private String issuedBy;
    private LocalDate issueDate;
    private String filePath;

    public Certificate(int userId, String certName, String issuedBy, LocalDate issueDate, String filePath) {
        this.userId = userId;
        this.certName = certName;
        this.issuedBy = issuedBy;
        this.issueDate = issueDate;
        this.filePath = filePath;
    }

    public Certificate(int certId, int userId, String certName, String issuedBy, LocalDate issueDate, String filePath) {
        this.certId = certId;
        this.userId = userId;
        this.certName = certName;
        this.issuedBy = issuedBy;
        this.issueDate = issueDate;
        this.filePath = filePath;
    }

    // Getters and Setters
    public int getCertId() { return certId; }
    public void setCertId(int certId) { this.certId = certId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getCertName() { return certName; }
    public void setCertName(String certName) { this.certName = certName; }
    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    @Override
    public String toString() {
        return "Certificate{id=" + certId + ", name=" + certName + ", issuedBy=" + issuedBy + ", date=" + issueDate + ", path=" + filePath + "}";
    }
}