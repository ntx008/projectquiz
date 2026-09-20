package com.quiz.oop.model;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class, Object & Encapsulation]
 * User: คลาสตัวแทนข้อมูลผู้ใช้งาน (นักศึกษา)
 * ============================================================================
 */
public class User {
    private final String studentId;
    private String password;
    private int highestScore;

    public User(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
        this.highestScore = 0;
    }

    public String getStudentId() {
        return studentId;
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

    public void updateScore(int score) {
        if (score > this.highestScore) {
            this.highestScore = score;
        }
    }
}
