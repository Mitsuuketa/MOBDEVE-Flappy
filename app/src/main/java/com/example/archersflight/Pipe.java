package com.example.archersflight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.content.res.Resources;

public class Pipe {
    private static final int GAP = 600; // Gap between top and bottom pipe
    private static final int PIPE_WIDTH = 200;
    private static final int PIPE_VELOCITY = 8;
    private Bitmap topPipeBitmap, bottomPipeBitmap;
    private Rect topPipe, bottomPipe;
    private float x;
    private boolean scored = false;

    public Pipe(int screenHeight, Resources resources) {
        // Load the top and bottom pipe images from resources
        topPipeBitmap = BitmapFactory.decodeResource(resources, R.drawable.toptube);
        bottomPipeBitmap = BitmapFactory.decodeResource(resources, R.drawable.bottomtube);

        // Randomize the height of the top pipe
        int randomHeight = (int) (Math.random() * (screenHeight - GAP));

        topPipe = new Rect();
        bottomPipe = new Rect();
        x = 1000; // Start the pipe far off-screen

        // Define the pipes' initial positions
        topPipe.set((int) x, 0, (int) (x + PIPE_WIDTH), randomHeight);
        bottomPipe.set((int) x, randomHeight + GAP, (int) (x + PIPE_WIDTH), screenHeight);
    }

    public void update() {
        x -= PIPE_VELOCITY; // Move pipes left
        topPipe.offset(-PIPE_VELOCITY, 0);
        bottomPipe.offset(-PIPE_VELOCITY, 0);
    }

    public void draw(Canvas canvas) {
        // Draw the pipes using the bitmap images
        canvas.drawBitmap(topPipeBitmap, null, topPipe, null);
        canvas.drawBitmap(bottomPipeBitmap, null, bottomPipe, null);
    }

    public boolean isOffScreen() {
        return x + PIPE_WIDTH < 0;
    }

    public Rect getTopPipe() {
        return topPipe;
    }

    public Rect getBottomPipe() {
        return bottomPipe;
    }

    public float getX() {
        return x;
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    public static int getPipeWidth() {
        return PIPE_WIDTH;
    }
}
