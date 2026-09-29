package com.example.cybersafetygame;

public class Question {
    private String location;
    private String questionText;
    private String[] choices;
    private int correctAnswerIndex;
    private String explanation;
    private int backgroundResId;

    public Question(String location, String questionText, String[] choices,
                    int correctAnswerIndex, String explanation, int backgroundResId) {
        this.location = location;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
        this.backgroundResId = backgroundResId;
    }

    public String getLocation() { return location; }
    public String getQuestionText() { return questionText; }
    public String[] getChoices() { return choices; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getExplanation() { return explanation; }
    public int getBackgroundResId() { return backgroundResId; }
}