package com.example.archersflight;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    GameThread gameThread;

    public GameView(Context context) {
        super(context);
        InitView();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        if(!gameThread.isRunning()){
            gameThread = new GameThread (surfaceHolder);
            gameThread.start();
        }else{
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

        if(gameThread.isRunning()){
            gameThread.setRunning(false);
            boolean retry = true;
            while(retry){
                try{
                    gameThread.join();
                    retry = false;
                }catch(InterruptedException e) {    }
            }
        }
    }

    void InitView(){
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        gameThread = new GameThread(holder);
    }
}
