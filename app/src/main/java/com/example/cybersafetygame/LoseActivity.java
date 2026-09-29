package com.example.cybersafetygame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoseActivity extends AppCompatActivity {

    private TextView tvSad, tvLoseScore;
    private MaterialButton btnTryAgain, btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        tvSad = findViewById(R.id.tvSad);
        tvLoseScore = findViewById(R.id.tvLoseScore);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Счёт из игры
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);
        tvLoseScore.setText("Правильных ответов: " + score + "/" + total);

        // Анимация грустного смайла
        startSadAnimation();

        // Кнопка «Попробовать снова»
        btnTryAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
            finish();
        });

        // Кнопка «В меню»
        btnBackToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void startSadAnimation() {
        // Плавное покачивание вниз-вверх (грустный вздох)
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(tvSad, "translationY", 0f, 10f);
        floatAnim.setDuration(2000);
        floatAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        floatAnim.start();

        // Лёгкое покачивание влево-вправо
        ObjectAnimator swayAnim = ObjectAnimator.ofFloat(tvSad, "rotation", -4f, 4f);
        swayAnim.setDuration(2500);
        swayAnim.setRepeatMode(ValueAnimator.REVERSE);
        swayAnim.setRepeatCount(ValueAnimator.INFINITE);
        swayAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        swayAnim.start();
    }
}