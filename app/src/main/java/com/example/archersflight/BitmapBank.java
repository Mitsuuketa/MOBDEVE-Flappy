package com.example.archersflight;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBank {

    Bitmap background_game;

    Bitmap[] bird;

    public BitmapBank(Resources resources){

        background_game = BitmapFactory.decodeResource(resources, R.drawable.background_game);

        background_game = scaleImage(background_game);

        bird = new Bitmap[4];
        bird[0] = BitmapFactory.decodeResource(resources, R.drawable.thebird);
        bird[1] = BitmapFactory.decodeResource(resources, R.drawable.thebird);
        bird[2] = BitmapFactory.decodeResource(resources, R.drawable.thebird);
        bird[3] = BitmapFactory.decodeResource(resources, R.drawable.thebird);
        for (int i = 0; i < bird.length; i++) {
            Bitmap originalBird = BitmapFactory.decodeResource(resources, R.drawable.thebird);
            bird[i] = scaleBitmap(originalBird, 0.19f); // Scale down to 50% of the original size
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
