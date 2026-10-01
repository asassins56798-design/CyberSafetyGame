package com.example.cybersafetygame;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class WinActivity extends AppCompatActivity {

    private TextView tvMedal, tvScore, tvCongrats;
    private MaterialButton btnPlayAgain, btnOpenStory, btnBackMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        tvMedal = findViewById(R.id.tvMedal);
        tvScore = findViewById(R.id.tvScore);
        tvCongrats = findViewById(R.id.tvCongrats);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnOpenStory = findViewById(R.id.btnOpenStory);
        btnBackMenu = findViewById(R.id.btnBackMenu);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 5);
        boolean fromStory = getIntent().getBooleanExtra("from_story", false);

        tvScore.setText("Правильных ответов: " + score + "/" + total);

        // Разные тексты в зависимости от режима
        if (fromStory) {
            // Это битва с боссом
            if (score == total) {
                tvCongrats.setText("🏆 МОЛОДЕЦ! Ты прошёл игру!\nТеперь ты умеешь защищать себя и близких в интернете");
            } else {
                tvCongrats.setText("Ты победил босса-мошенника!\nдля звания мастера кибербезопасности.");
            }
            // Скрываем кнопку «Открыть Историю» — история уже пройдена
            btnOpenStory.setVisibility(android.view.View.GONE);
        } else {
            // Это «Новая игра»
            if (score == total) {
                tvCongrats.setText("🏆 МОЛОДЕЦ! Ты стал мастером кибербезопасности!\nТеперь сразись с боссом в Истории!");
            } else {
                tvCongrats.setText("Ты прошёл испытание!\nТеперь сразись с боссом в Истории!");
            }
        }

        startMedalAnimation();

        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
            finish();
        });

        btnOpenStory.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            startActivity(intent);
            finish();
        });

        btnBackMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void startMedalAnimation() {
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(tvMedal, "translationY", 0f, -18f);
        floatAnim.setDuration(1800);
        floatAnim.setRepeatMode(ValueAnimator.REVERSE);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        floatAnim.start();

        ObjectAnimator swayAnim = ObjectAnimator.ofFloat(tvMedal, "rotation", -5f, 5f);
        swayAnim.setDuration(2200);
        swayAnim.setRepeatMode(ValueAnimator.REVERSE);
        swayAnim.setRepeatCount(ValueAnimator.INFINITE);
        swayAnim.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        swayAnim.start();
    }
}