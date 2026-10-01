package com.example.cybersafetygame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private List<BossScene> scenes;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;
    private boolean canProceed = false;
    private boolean waitingForTap = false;

    private TextView tvBossProgress, tvBossAttack, tvStoryQuestion;
    private Button btnStoryOption1, btnStoryOption2, btnStoryOption3;
    private ImageView ivStoryBg;
    private View cardStoryQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ivStoryBg = findViewById(R.id.ivStoryBg);
        tvBossProgress = findViewById(R.id.tvBossProgress);
        tvBossAttack = findViewById(R.id.tvBossAttack);
        tvStoryQuestion = findViewById(R.id.tvStoryQuestion);
        btnStoryOption1 = findViewById(R.id.btnStoryOption1);
        btnStoryOption2 = findViewById(R.id.btnStoryOption2);
        btnStoryOption3 = findViewById(R.id.btnStoryOption3);
        cardStoryQuestion = findViewById(R.id.cardStoryQuestion);

        scenes = BossData.getScenes();

        btnStoryOption1.setOnClickListener(v -> checkAnswer(0));
        btnStoryOption2.setOnClickListener(v -> checkAnswer(1));
        btnStoryOption3.setOnClickListener(v -> checkAnswer(2));

        // ТАП по карточке = следующая атака
        cardStoryQuestion.setOnClickListener(v -> {
            if (waitingForTap && canProceed) {
                proceedToNext();
            }
        });

        // Тап в любом месте экрана
        View root = findViewById(android.R.id.content);
        root.setOnClickListener(v -> {
            if (waitingForTap && canProceed) {
                proceedToNext();
            }
        });

        showScene();
    }

    private void showScene() {
        if (currentIndex >= scenes.size()) {
            finishGame();
            return;
        }

        answered = false;
        canProceed = false;
        waitingForTap = false;

        BossScene scene = scenes.get(currentIndex);

        tvBossProgress.setText("Вопрос " + (currentIndex + 1) + "/" + scenes.size());
        tvBossAttack.setText("👹 БОСС: «" + scene.getAttack() + "»");
        tvStoryQuestion.setText("Как ты ответишь?");

        ivStoryBg.setAlpha(0f);
        ivStoryBg.setImageResource(scene.getBackgroundResId());
        ivStoryBg.animate().alpha(1f).setDuration(400).start();

        btnStoryOption1.setText(scene.getOptions()[0]);
        btnStoryOption2.setText(scene.getOptions()[1]);
        btnStoryOption3.setText(scene.getOptions()[2]);

        resetButtonColors();
        setButtonsEnabled(true);
    }

    private void checkAnswer(int selected) {
        if (answered) return;
        answered = true;
        setButtonsEnabled(false);

        BossScene scene = scenes.get(currentIndex);
        Button[] buttons = {btnStoryOption1, btnStoryOption2, btnStoryOption3};

        if (selected == scene.getCorrectIndex()) {
            score++;
            buttons[selected].getBackground().setTint(0xFF388E3C);
            tvStoryQuestion.setText(scene.getExplanation()
                    + "\n\n👆 Нажми, чтобы продолжить");
        } else {
            buttons[selected].getBackground().setTint(0xFFC62828);
            buttons[scene.getCorrectIndex()].getBackground().setTint(0xFF388E3C);
            tvStoryQuestion.setText(scene.getExplanation()
                    + "\n\n👆 Нажми, чтобы продолжить");
        }

        waitingForTap = true;
        new Handler(Looper.getMainLooper()).postDelayed(
                () -> canProceed = true, 300);
    }

    private void proceedToNext() {
        waitingForTap = false;
        canProceed = false;
        currentIndex++;
        showScene();
    }

    private void resetButtonColors() {
        btnStoryOption1.getBackground().setTint(0xFFF39C12);
        btnStoryOption2.getBackground().setTint(0xFFF39C12);
        btnStoryOption3.getBackground().setTint(0xFFF39C12);
    }

    private void setButtonsEnabled(boolean enabled) {
        btnStoryOption1.setEnabled(enabled);
        btnStoryOption2.setEnabled(enabled);
        btnStoryOption3.setEnabled(enabled);
    }

    private void finishGame() {
        int passingScore = 4;
        boolean isVictory = score >= passingScore;

        Intent intent;
        if (isVictory) {
            intent = new Intent(this, WinActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", scenes.size());
            intent.putExtra("from_story", true);
        } else {
            intent = new Intent(this, LoseActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", scenes.size());
        }
        startActivity(intent);
        finish();
    }
}