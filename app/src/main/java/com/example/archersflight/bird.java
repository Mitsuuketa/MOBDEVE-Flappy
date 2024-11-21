package com.example.archersflight;

public class bird {

    private int birdX, birdY, currentFrame, velocity;
    public static int maxFrame;

    public bird(){

        birdX = AppConstants.SCREEN_WIDTH/4 - AppConstants.getBitmapBank().getBirdWidth()/4;
        birdY = AppConstants.SCREEN_HEIGHT/4 - AppConstants.getBitmapBank().getBirdHeight()/4;
        currentFrame = 0;
        maxFrame =3;
        velocity = 0;
    }

    public int getVelocity(){
        return velocity;
    }

    public void setVelocity(int velocity){
        this.velocity = velocity;
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame){
        this.currentFrame = currentFrame;
    }

    public int getX(){
        return birdX;
    }

    public int getY(){
        return birdY;
    }

    public void setX(int birdX){
        this.birdX = birdX;
    }

    public void setY(int birdY){
        this.birdY = birdY;
    }
}
