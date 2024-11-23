package com.example.archersflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    //tap sound effect
    private MediaPlayer clickSoundPlayer;

    GameThread gameThread;

    public GameView(Context context) {
        super(context);
        InitView();

        // Initialize MediaPlayer in the constructor
        clickSoundPlayer = MediaPlayer.create(context, R.raw.tap_effect);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        if (!gameThread.isRunning()) {
            gameThread = new GameThread(surfaceHolder);
            gameThread.start();
        } else {
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

        if (gameThread.isRunning()) {
            gameThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }
        // Release MediaPlayer resources
        if (clickSoundPlayer != null) {
            clickSoundPlayer.release();
            clickSoundPlayer = null;
        }
    }

    void InitView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        gameThread = new GameThread(holder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE);
        boolean isTapEffectEnabled = sharedPreferences.getBoolean("tap_effect_enabled", true);


        if (action == MotionEvent.ACTION_DOWN) {
            if (AppConstants.getGameEngine().gameState == 2) {
                // Game over state: Restart or Go Back logic
                float x = event.getX();
                float y = event.getY();

                if (AppConstants.getGameEngine().tryAgainButtonRect.contains((int) x, (int) y)) {
                    AppConstants.getGameEngine().resetGame(); // Restart the game
                } else if (AppConstants.getGameEngine().backButtonRect.contains((int) x, (int) y)) {
                    ((Activity) getContext()).finish(); // Exit the GameActivity and go back
                }
            } else {
                AppConstants.getGameEngine().gameState = 1;
                AppConstants.getGameEngine().bird.setVelocity(AppConstants.VELOCITY_WHEN_JUMPED);
            }

            if (isTapEffectEnabled && clickSoundPlayer != null) {
                clickSoundPlayer.start(); // Play click sound
            }
        }
        return true;
    }
}