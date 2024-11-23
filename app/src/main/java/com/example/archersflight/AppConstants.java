package com.example.archersflight;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class AppConstants {

    static BitmapBank bitmapBank;

    static int SCREEN_WIDTH, SCREEN_HEIGHT;

    static int gravity;

    static int VELOCITY_WHEN_JUMPED;

    static GameEngine gameEngine;

    static MediaPlayer clickSoundPlayer;

    public static void initialization(Context context) {
        setScreenSize(context);
        bitmapBank = new BitmapBank(context.getResources(), context);
        gameEngine = new GameEngine(context); // Pass the context here
        clickSoundPlayer = MediaPlayer.create(context, R.raw.tap_effect);

        AppConstants.gravity = 3;
        AppConstants.VELOCITY_WHEN_JUMPED = -40;
    }


    public static BitmapBank getBitmapBank(){
        return bitmapBank;
    }

    public static GameEngine getGameEngine(){
        return gameEngine;
    }

    public static void setScreenSize(Context context){
        WindowManager wm = (android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        AppConstants.SCREEN_WIDTH = width;
        AppConstants.SCREEN_HEIGHT = height;
    }

    // Play the click sound
    public static void playClickSound() {
        if (clickSoundPlayer != null) {
            clickSoundPlayer.start();
        }
    }
    // Release MediaPlayer resources
    public static void releaseResources() {
        if (clickSoundPlayer != null) {
            clickSoundPlayer.release();
            clickSoundPlayer = null;
        }
    }

}
