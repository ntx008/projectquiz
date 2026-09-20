package com.quiz.oop.model;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class and Object]
 * QuizQuestion: คลาสตัวแทนคำถามปัญหาเชาว์ปัญญา 1 ข้อ
 * ============================================================================
 */
public class QuizQuestion {
    private final String question;
    private final String[] choices;
    private final int correctIndex;
    private final String explanation;
    private final String difficulty;

    public QuizQuestion(String question, String[] choices, int correctIndex, String explanation, String difficulty) {
        this.question = question;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
        this.difficulty = (difficulty != null && !difficulty.isBlank()) ? difficulty.toLowerCase() : "easy";
    }

    public QuizQuestion(String question, String[] choices, int correctIndex, String explanation) {
        this(question, choices, correctIndex, explanation, "easy");
    }

    private String id;

    public String getQuestion() { return question; }
    public String[] getChoices() { return choices; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }
    public String getDifficulty() { return difficulty; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public boolean isCorrect(int choiceIndex) {
        return choiceIndex == correctIndex;
    }
}
