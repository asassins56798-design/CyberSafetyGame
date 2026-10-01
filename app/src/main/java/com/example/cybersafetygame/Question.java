package com.example.cybersafetygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * Перемешивает варианты ответов и обновляет индекс правильного.
     */
    public void shuffleChoices() {
        // Сохраняем правильный ответ
        String correctAnswer = choices[correctAnswerIndex];

        // Перемешиваем варианты
        List<String> list = new ArrayList<>();
        Collections.addAll(list, choices);
        Collections.shuffle(list);

        // Обратно в массив
        for (int i = 0; i < choices.length; i++) {
            choices[i] = list.get(i);
        }

        // Находим новый индекс правильного ответа
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].equals(correctAnswer)) {
                correctAnswerIndex = i;
                break;
            }
        }
    }
}