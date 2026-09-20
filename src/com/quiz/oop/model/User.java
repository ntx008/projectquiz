package com.quiz.oop.model;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class, Object & Encapsulation]
 * User: คลาสตัวแทนข้อมูลผู้ใช้งาน (นักศึกษา)
 * ============================================================================
 */
public class User {
    public static final String ADMIN_ID = "6812247008";

    private final String studentId;
    private String password;
    private int highestScore;
    private int totalScore;
    private int quizzesPlayed;
    private final String role;

    public User(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
        this.highestScore = 0;
        this.totalScore = 0;
        this.quizzesPlayed = 0;
        this.role = ADMIN_ID.equals(studentId != null ? studentId.trim() : "") ? "admin" : "player";
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role) || ADMIN_ID.equals(studentId);
    }

    public String getRole() {
        return role;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getQuizzesPlayed() {
        return quizzesPlayed;
    }

    public void addScore(int score) {
        this.quizzesPlayed++;
        this.totalScore += score;
        if (score > this.highestScore) {
            this.highestScore = score;
        }
    }

    public void updateScore(int score) {
        addScore(score);
    }

    public void resetScores() {
        this.highestScore = 0;
        this.totalScore = 0;
        this.quizzesPlayed = 0;
    }
}
