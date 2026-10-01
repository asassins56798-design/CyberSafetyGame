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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final long INTRO_DURATION_MS = 5000;

    private MaterialButton btnNewGame, btnStory;
    private ImageView ivKid;
    private TextView tvTitle, tvSubtitle, tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewGame = findViewById(R.id.btnNewGame);
        btnStory = findViewById(R.id.btnStory);
        ivKid = findViewById(R.id.ivKid);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvVersion = findViewById(R.id.tvVersion);

        // Обводка для SMART BOY через reflection — работает на всех версиях
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            try {
                java.lang.reflect.Method setStrokeColor = TextView.class.getMethod(
                        "setStrokeTextColor", int.class);
                setStrokeColor.invoke(tvTitle, 0xFF1A1A1A);

                java.lang.reflect.Method setStrokeWidth = TextView.class.getMethod(
                        "setStrokeTextWidth", float.class);
                setStrokeWidth.invoke(tvTitle, 12f);
            } catch (Exception e) {
                tvTitle.setShadowLayer(6f, 2f, 2f, 0xFF1A1A1A);
            }
        } else {
            tvTitle.setShadowLayer(6f, 2f, 2f, 0xFF1A1A1A);
        }

        btnNewGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransitionActivity.class);
            startActivity(intent);
        });

        btnStory.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            startActivity(intent);
        });

        startEntranceAnimation();
        playIntroThenLoop();
    }

    private void playIntroThenLoop() {
        Glide.with(this)
                .asGif()
                .load(R.drawable.kid_intro)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<GifDrawable> target, boolean isFirstResource) {
                        loadLoopAnimation();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model,
                                                   Target<GifDrawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        new Handler(Looper.getMainLooper()).postDelayed(
                                MainActivity.this::loadLoopAnimation,
                                INTRO_DURATION_MS);
                        return false;
                    }
                })
                .into(ivKid);
    }

    private void loadLoopAnimation() {
        Glide.with(this)
                .asGif()
                .load(R.drawable.kid_loop)
                .into(ivKid);
    }

    private void startEntranceAnimation() {
        fadeIn(tvTitle, 200);
        fadeIn(tvSubtitle, 500);
        fadeIn(btnNewGame, 700);
        fadeIn(btnStory, 900);
        fadeIn(tvVersion, 1100);

        new Handler(Looper.getMainLooper()).postDelayed(
                this::startSynchronizedPulse, 1500);
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

    private void startSynchronizedPulse() {
        int duration = 1500;

        // === КНОПКА «НАЧАТЬ ИГРУ» ===
        ObjectAnimator btnNewPulseX = ObjectAnimator.ofFloat(btnNewGame, "scaleX", 1f, 1.05f);
        btnNewPulseX.setDuration(duration);
        btnNewPulseX.setRepeatMode(ValueAnimator.REVERSE);
        btnNewPulseX.setRepeatCount(ValueAnimator.INFINITE);
        btnNewPulseX.setInterpolator(new AccelerateDecelerateInterpolator());
        btnNewPulseX.start();

        ObjectAnimator btnNewPulseY = ObjectAnimator.ofFloat(btnNewGame, "scaleY", 1f, 1.05f);
        btnNewPulseY.setDuration(duration);
        btnNewPulseY.setRepeatMode(ValueAnimator.REVERSE);
        btnNewPulseY.setRepeatCount(ValueAnimator.INFINITE);
        btnNewPulseY.setInterpolator(new AccelerateDecelerateInterpolator());
        btnNewPulseY.start();

        // === КНОПКА «ИСТОРИЯ» ===
        ObjectAnimator btnStoryPulseX = ObjectAnimator.ofFloat(btnStory, "scaleX", 1f, 1.05f);
        btnStoryPulseX.setDuration(duration);
        btnStoryPulseX.setRepeatMode(ValueAnimator.REVERSE);
        btnStoryPulseX.setRepeatCount(ValueAnimator.INFINITE);
        btnStoryPulseX.setInterpolator(new AccelerateDecelerateInterpolator());
        btnStoryPulseX.start();

        ObjectAnimator btnStoryPulseY = ObjectAnimator.ofFloat(btnStory, "scaleY", 1f, 1.05f);
        btnStoryPulseY.setDuration(duration);
        btnStoryPulseY.setRepeatMode(ValueAnimator.REVERSE);
        btnStoryPulseY.setRepeatCount(ValueAnimator.INFINITE);
        btnStoryPulseY.setInterpolator(new AccelerateDecelerateInterpolator());
        btnStoryPulseY.start();

        // === ПОДЗАГОЛОВОК ===
        tvSubtitle.post(() -> {
            tvSubtitle.setPivotX(tvSubtitle.getWidth() / 2f);
            tvSubtitle.setPivotY(tvSubtitle.getHeight() / 2f);
        });

        ObjectAnimator subtitlePulseX = ObjectAnimator.ofFloat(tvSubtitle, "scaleX", 1f, 1.02f);
        subtitlePulseX.setDuration(duration);
        subtitlePulseX.setRepeatMode(ValueAnimator.REVERSE);
        subtitlePulseX.setRepeatCount(ValueAnimator.INFINITE);
        subtitlePulseX.setInterpolator(new AccelerateDecelerateInterpolator());
        subtitlePulseX.start();

        ObjectAnimator subtitlePulseY = ObjectAnimator.ofFloat(tvSubtitle, "scaleY", 1f, 1.02f);
        subtitlePulseY.setDuration(duration);
        subtitlePulseY.setRepeatMode(ValueAnimator.REVERSE);
        subtitlePulseY.setRepeatCount(ValueAnimator.INFINITE);
        subtitlePulseY.setInterpolator(new AccelerateDecelerateInterpolator());
        subtitlePulseY.start();
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