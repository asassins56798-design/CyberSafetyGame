package com.example.cybersafetygame;

public class BossScene {
    private String attack;         // Атака босса (что он пишет/говорит)
    private String[] options;      // 3 варианта ответа
    private int correctIndex;      // Правильный ответ
    private String explanation;    // Объяснение после ответа
    private int backgroundResId;   // Фон сцены

    public BossScene(String attack, String[] options,
                     int correctIndex, String explanation, int backgroundResId) {
        this.attack = attack;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
        this.backgroundResId = backgroundResId;
    }

    public String getAttack() { return attack; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }
    public int getBackgroundResId() { return backgroundResId; }

    /**
     * Перемешивает варианты ответов и обновляет индекс правильного.
     */
    public void shuffleChoices() {
        String correctAnswer = options[correctIndex];

        java.util.List<String> list = new java.util.ArrayList<>();
        java.util.Collections.addAll(list, options);
        java.util.Collections.shuffle(list);

        for (int i = 0; i < options.length; i++) {
            options[i] = list.get(i);
        }

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(correctAnswer)) {
                correctIndex = i;
                break;
            }
        }
    }
}