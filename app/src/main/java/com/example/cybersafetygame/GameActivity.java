package com.example.cybersafetygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private boolean canProceed = false;    // можно ли идти дальше
    private boolean waitingForTap = false; // ждём клик

    private TextView tvLocation, tvProgress, tvQuestion;
    private Button btnOption1, btnOption2, btnOption3;
    private ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ivBackground = findViewById(R.id.ivBackground);
        tvLocation = findViewById(R.id.tvLocation);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);

        questions = QuizData.getQuestions();

        btnOption1.setOnClickListener(v -> checkAnswer(0));
        btnOption2.setOnClickListener(v -> checkAnswer(1));
        btnOption3.setOnClickListener(v -> checkAnswer(2));

        // Клик в любое место экрана = продолжить
        View root = findViewById(android.R.id.content);
        root.setOnClickListener(v -> {
            if (waitingForTap && canProceed) {
                proceedToNext();
            }
        });

        // Клик по карточке вопроса тоже работает
        tvQuestion.setOnClickListener(v -> {
            if (waitingForTap && canProceed) {
                proceedToNext();
            }
        });

        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            finishGame();
            return;
        }

        answered = false;
        canProceed = false;
        waitingForTap = false;

        Question q = questions.get(currentIndex);

        tvLocation.setText(q.getLocation());
        tvProgress.setText("Вопрос " + (currentIndex + 1) + "/" + questions.size());
        tvQuestion.setText(q.getQuestionText());

        ivBackground.setAlpha(0f);
        ivBackground.setImageResource(q.getBackgroundResId());
        ivBackground.animate().alpha(1f).setDuration(400).start();

        btnOption1.setText(q.getChoices()[0]);
        btnOption2.setText(q.getChoices()[1]);
        btnOption3.setText(q.getChoices()[2]);

        resetButtonColors();
        setButtonsEnabled(true);
    }

    private void checkAnswer(int selected) {
        if (answered) return;
        answered = true;
        setButtonsEnabled(false);

        Question q = questions.get(currentIndex);
        Button[] buttons = {btnOption1, btnOption2, btnOption3};

        if (selected == q.getCorrectAnswerIndex()) {
            score++;
            buttons[selected].getBackground().setTint(0xFF388E3C);
            tvQuestion.setText(q.getQuestionText()
                    + "\n\n✅ Верно!\n"
                    + q.getExplanation()
                    + "\n\n👆 Нажми, чтобы продолжить");
        } else {
            buttons[selected].getBackground().setTint(0xFFC62828);
            buttons[q.getCorrectAnswerIndex()].getBackground().setTint(0xFF388E3C);
            tvQuestion.setText(q.getQuestionText()
                    + "\n\n❌ Правильный ответ:\n"
                    + q.getExplanation()
                    + "\n\n👆 Нажми, чтобы продолжить");
        }

        // Разрешаем переход через 0.3 секунды (защита от случайного тапа)
        waitingForTap = true;
        new Handler(Looper.getMainLooper()).postDelayed(
                () -> canProceed = true, 300);
    }

    private void proceedToNext() {
        waitingForTap = false;
        canProceed = false;
        currentIndex++;
        showQuestion();
    }

    private void resetButtonColors() {
        btnOption1.getBackground().setTint(0xFFF39C12);
        btnOption2.getBackground().setTint(0xFFF39C12);
        btnOption3.getBackground().setTint(0xFFF39C12);
    }

    private void setButtonsEnabled(boolean enabled) {
        btnOption1.setEnabled(enabled);
        btnOption2.setEnabled(enabled);
        btnOption3.setEnabled(enabled);
    }

    private void finishGame() {
        // Разблокируем историю
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("story_unlocked", true).apply();

        // Проходной балл — 8 из 10
        int passingScore = 8;
        boolean isVictory = score >= passingScore;

        Intent intent;
        if (isVictory) {
            intent = new Intent(this, WinActivity.class);
        } else {
            intent = new Intent(this, LoseActivity.class);
        }

        intent.putExtra("score", score);
        intent.putExtra("total", questions.size());
        startActivity(intent);
        finish();
    }
}