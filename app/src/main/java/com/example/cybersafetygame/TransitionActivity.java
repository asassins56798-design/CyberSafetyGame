package com.example.cybersafetygame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TransitionActivity extends AppCompatActivity {

    private ImageView ivShieldLeft, ivShieldRight, ivLock;
    private MediaPlayer clangSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        // ===== МЯГКИЙ FULLSCREEN =====
        // Растягиваем контент под системные панели
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Убираем ТОЛЬКО статус-бар, НЕ трогаем навигацию
        // (не показывается системное уведомление про fullscreen)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        // =============================

        ivShieldLeft = findViewById(R.id.ivShieldLeft);
        ivShieldRight = findViewById(R.id.ivShieldRight);
        ivLock = findViewById(R.id.ivLock);

        // Загружаем звук смыкания
        clangSound = MediaPlayer.create(this, R.raw.clang);

        // Узнаём реальную ширину экрана в пикселях
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int halfWidth = screenWidth / 2;

        // Задаём щитам ширину ровно половину экрана
        ivShieldLeft.getLayoutParams().width = halfWidth;
        ivShieldLeft.requestLayout();

        ivShieldRight.getLayoutParams().width = halfWidth;
        ivShieldRight.requestLayout();

        // Ставим их за экраном изначально
        ivShieldLeft.setTranslationX(-screenWidth);
        ivShieldRight.setTranslationX(screenWidth);

        // Запускаем анимацию после того, как layout применён
        ivShieldLeft.post(() -> startShieldAnimation(screenWidth));
    }

    private void startShieldAnimation(int screenWidth) {
        int moveDuration = 400;

        // Левый щит — едет к центру
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(
                ivShieldLeft, "translationX", -screenWidth, 0f);
        leftAnim.setDuration(moveDuration);
        leftAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        // Правый щит — едет к центру
        ObjectAnimator rightAnim = ObjectAnimator.ofFloat(
                ivShieldRight, "translationX", screenWidth, 0f);
        rightAnim.setDuration(moveDuration);
        rightAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        leftAnim.start();
        rightAnim.start();

        // Когда щиты сомкнулись — звук, тряска, замок
        rightAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (clangSound != null) {
                    clangSound.start();
                }
                shakeShields();
                showLock();
            }
        });
    }

    private void shakeShields() {
        ivShieldLeft.animate().translationXBy(10f).setDuration(50)
                .withEndAction(() -> ivShieldLeft.animate().translationXBy(-10f)
                        .setDuration(50).start()).start();
        ivShieldRight.animate().translationXBy(-10f).setDuration(50)
                .withEndAction(() -> ivShieldRight.animate().translationXBy(10f)
                        .setDuration(50).start()).start();
    }

    private void showLock() {
        ivLock.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(350)
                .setInterpolator(new OvershootInterpolator(2f))
                .withEndAction(() -> ivLock.postDelayed(() -> {
                    Intent intent = new Intent(TransitionActivity.this, GameActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    finish();
                }, 200))
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clangSound != null) {
            clangSound.release();
            clangSound = null;
        }
    }
}