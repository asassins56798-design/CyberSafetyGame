package com.example.cybersafetygame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnNewGame, btnStory;
    private TextView tvLogo, tvTitle, tvSubtitle, tvVersion;
    private View vDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewGame = findViewById(R.id.btnNewGame);
        btnStory = findViewById(R.id.btnStory);
        tvLogo = findViewById(R.id.tvLogo);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvVersion = findViewById(R.id.tvVersion);
        vDivider = findViewById(R.id.vDivider);

        btnNewGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
        });

        btnStory.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            startActivity(intent);
        });

        // Запускаем последовательное появление всех элементов
        startEntranceAnimation();
    }

    private void startEntranceAnimation() {
        // Задержки для каждого элемента
        fadeIn(tvLogo, 0);
        fadeIn(tvTitle, 200);
        fadeIn(vDivider, 350);
        fadeIn(tvSubtitle, 500);
        fadeIn(btnNewGame, 700);
        fadeIn(btnStory, 900);
        fadeIn(tvVersion, 1100);

        // Пульсация кнопки «НАЧАТЬ ИГРУ» — стартует после появления
        new Handler(Looper.getMainLooper()).postDelayed(
                this::startPulseAnimation, 1500);

        // Покачивание логотипа
        startLogoAnimation();
    }

    private void fadeIn(View view, long delayMs) {
        view.setAlpha(0f);
        view.setTranslationY(30f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(delayMs)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private void startPulseAnimation() {
        ObjectAnimator pulse = ObjectAnimator.ofFloat(btnNewGame, "scaleX", 1f, 1.05f);
        pulse.setDuration(1200);
        pulse.setRepeatMode(ValueAnimator.REVERSE);
        pulse.setRepeatCount(ValueAnimator.INFINITE);
        pulse.setInterpolator(new AccelerateDecelerateInterpolator());
        pulse.start();

        ObjectAnimator pulseY = ObjectAnimator.ofFloat(btnNewGame, "scaleY", 1f, 1.05f);
        pulseY.setDuration(1200);
        pulseY.setRepeatMode(ValueAnimator.REVERSE);
        pulseY.setRepeatCount(ValueAnimator.INFINITE);
        pulseY.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseY.start();
    }

    private void startLogoAnimation() {
        // Плавное покачивание логотипа
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(tvLogo, "translationY", 0f, -12f);
        floatAnim.setDuration(2200);
        floatAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        floatAnim.setStartDelay(1200);
        floatAnim.start();

        // Лёгкое покачивание влево-вправо
        ObjectAnimator swayAnim = ObjectAnimator.ofFloat(tvLogo, "rotation", -3f, 3f);
        swayAnim.setDuration(2800);
        swayAnim.setRepeatMode(ValueAnimator.REVERSE);
        swayAnim.setRepeatCount(ValueAnimator.INFINITE);
        swayAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        swayAnim.setStartDelay(1200);
        swayAnim.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        boolean unlocked = prefs.getBoolean("story_unlocked", false);

        btnStory.setEnabled(unlocked);
        btnStory.setAlpha(unlocked ? 1.0f : 0.45f);
    }
}