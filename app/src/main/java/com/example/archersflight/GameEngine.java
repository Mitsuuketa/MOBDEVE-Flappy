package com.example.archersflight;

import android.graphics.Canvas;

public class GameEngine {

    BackgroundImage backgroundImage;
    public GameEngine(){

        backgroundImage = new BackgroundImage();

    }

    public void updateAndDrawableBackgroundImage(Canvas canvas){
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        if (backgroundImage.getX() <- AppConstants.getBitmapBank().getBackgroundWidth()){
            backgroundImage.setX(0);
        }
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX(),
                backgroundImage.getY(),null);

        if(backgroundImage.getX() <- (AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH)){
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX()+AppConstants.getBitmapBank().getBackgroundWidth(),backgroundImage.getY(), null);
        }
    }
}
