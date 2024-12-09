package com.example.archersflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBank {

    Bitmap background_game;
    Bitmap[] bird;

    public BitmapBank(Resources resources, Context context) {
        // Load the game background
        background_game = BitmapFactory.decodeResource(resources, R.drawable.background_game);
        background_game = scaleImage(background_game);

        // Retrieve the selected avatar from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        int selectedAvatarRes = sharedPreferences.getInt("selected_avatar", R.drawable.default_avatar); // Default if none is selected

        // Load the selected avatar's image and scale it
        bird = new Bitmap[4];
        for (int i = 0; i < bird.length; i++) {
            Bitmap originalBird = BitmapFactory.decodeResource(resources, selectedAvatarRes);
            bird[i] = scaleBitmap(originalBird, 0.075f); // Scale down to 7.5%
        }
    }

    public Bitmap scaleBitmap(Bitmap bitmap, float scaleFactor) {
        int width = (int) (bitmap.getWidth() * scaleFactor);
        int height = (int) (bitmap.getHeight() * scaleFactor);
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public Bitmap getBird (int frame){
        return bird [frame];
    }

    public int getBirdWidth(){
        return bird [0].getWidth();
    }

    public int getBirdHeight(){
        return bird [0].getHeight();
    }

    public Bitmap getBackground_game(){
        return background_game;
    }

    public int getBackgroundWidth(){
        return background_game.getWidth();
    }

    public int getBackgroundHeight(){
        return background_game.getHeight();
    }

    public Bitmap scaleImage(Bitmap bitmap){
        float widthHeightRatio = getBackgroundWidth() / getBackgroundHeight();
        int backgroundScaleWidth = (int) widthHeightRatio * AppConstants.SCREEN_HEIGHT;
        return Bitmap.createScaledBitmap(bitmap, backgroundScaleWidth, AppConstants.SCREEN_HEIGHT, false);
    }

}