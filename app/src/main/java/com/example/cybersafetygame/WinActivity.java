package com.example.cybersafetygame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WinActivity extends AppCompatActivity {

    private TextView tvMedal, tvScore;
    private MaterialButton btnPlayAgain, btnOpenStory, btnBackMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        tvMedal = findViewById(R.id.tvMedal);
        tvScore = findViewById(R.id.tvScore);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnOpenStory = findViewById(R.id.btnOpenStory);
        btnBackMenu = findViewById(R.id.btnBackMenu);

        // Получаем счёт из предыдущего экрана
        int score = getIntent().getIntExtra("score", 10);
        int total = getIntent().getIntExtra("total", 10);
        tvScore.setText("Правильных ответов: " + score + "/" + total);

        // Анимация медали
        startMedalAnimation();

        // Кнопка «Играть ещё» — перезапустить игру
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
            finish();
        });

        // Кнопка «Открыть Историю»
        btnOpenStory.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            startActivity(intent);
            finish();
        });

        // Кнопка «В меню»
        btnBackMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void startMedalAnimation() {
        // Покачивание вверх-вниз
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(tvMedal, "translationY", 0f, -18f);
        floatAnim.setDuration(1800);
        floatAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        floatAnim.start();

        // Лёгкое покачивание влево-вправо
        ObjectAnimator swayAnim = ObjectAnimator.ofFloat(tvMedal, "rotation", -5f, 5f);
        swayAnim.setDuration(2200);
        swayAnim.setRepeatMode(ValueAnimator.REVERSE);
        swayAnim.setRepeatCount(ValueAnimator.INFINITE);
        swayAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        swayAnim.start();
    }
}