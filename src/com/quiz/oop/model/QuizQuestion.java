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

    public QuizQuestion(String question, String[] choices, int correctIndex, String explanation) {
        this.question = question;
        this.choices = choices;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getQuestion() { return question; }
    public String[] getChoices() { return choices; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }

    public boolean isCorrect(int choiceIndex) {
        return choiceIndex == correctIndex;
    }
}
