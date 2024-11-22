package com.example.archersflight;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameEngine {

    BackgroundImage backgroundImage;
    bird bird;

    static int gameState;

    private List<Pipe> pipes; // List to manage pipes
    private int pipeSpawnTime = 100; // Time between pipe spawns (frames)
    private int pipeTimer = 0;
    int score = 0;

    Rect tryAgainButtonRect;
    Rect backButtonRect;

    public GameEngine() {
        backgroundImage = new BackgroundImage();
        bird = new bird();
        pipes = new ArrayList<>(); // Initialize the pipe list
        gameState = 0; // Initial game state
    }

    public void updateAndDrawableBackgroundImage(Canvas canvas) {
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        if (backgroundImage.getX() < -AppConstants.getBitmapBank().getBackgroundWidth()) {
            backgroundImage.setX(0);
        }
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX(),
                backgroundImage.getY(), null);

        if (backgroundImage.getX() < (AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH)) {
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(),
                    backgroundImage.getX() + AppConstants.getBitmapBank().getBackgroundWidth(),
                    backgroundImage.getY(), null);
        }
    }

    public void updateAndDrawBird(Canvas canvas) {
        if (gameState == 1) {
            if (bird.getY() < (AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getBirdHeight())
                    || bird.getVelocity() < 0) {
                bird.setVelocity(bird.getVelocity() + AppConstants.gravity);
                bird.setY(bird.getY() + bird.getVelocity());
            }
        }

        int currentFrame = bird.getCurrentFrame();
        canvas.drawBitmap(AppConstants.getBitmapBank().getBird(currentFrame), bird.getX(), bird.getY(), null);
        currentFrame++;

        if (currentFrame > bird.maxFrame) {
            currentFrame = 0;
        }
        bird.setCurrentFrame(currentFrame);
    }

    public void updateAndDrawObstacles(Canvas canvas) {
        Paint paint = new Paint(); // Shared Paint object for drawing score and messages

        if (gameState == 1) {
            // Spawn pipes at intervals
            if (pipeTimer == 0) {
                pipes.add(new Pipe(AppConstants.SCREEN_HEIGHT)); // Add new pipe
                Log.d("GameEngine", "Pipe spawned! Total pipes: " + pipes.size());
                pipeTimer = pipeSpawnTime; // Reset the timer
            } else {
                pipeTimer--; // Decrement the timer
            }

            // Move pipes and remove off-screen pipes
            Iterator<Pipe> iterator = pipes.iterator();
            while (iterator.hasNext()) {
                Pipe pipe = iterator.next();
                pipe.update();

                if (pipe.isOffScreen()) {
                    iterator.remove(); // Remove off-screen pipes
                }
            }

            // Draw pipes and check for collisions
            for (Pipe pipe : pipes) {
                pipe.draw(canvas);
                if (!pipe.isScored() && pipe.getX() + Pipe.getPipeWidth() < bird.getX()) {
                    score++;
                    pipe.setScored(true);
                    Log.d("GameEngine", "Score: " + score);
                }

                // Check for collision
                Rect birdRect = new Rect(
                        bird.getX(),
                        bird.getY(),
                        bird.getX() + AppConstants.getBitmapBank().getBirdWidth(),
                        bird.getY() + AppConstants.getBitmapBank().getBirdHeight()
                );

                if (Rect.intersects(birdRect, pipe.getTopPipe()) || Rect.intersects(birdRect, pipe.getBottomPipe())) {
                    gameState = 2; // Game over state
                    Log.d("GameEngine", "Game Over! Bird collided with a pipe.");
                }
            }

            // Draw the current score during the game
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText("Score: " + score, 50, 100, paint);

        } else if (gameState == 2) {
            // Draw "Game Over" message
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game Over!", AppConstants.SCREEN_WIDTH / 2 - 200, AppConstants.SCREEN_HEIGHT / 2 - 100, paint);

            // Draw "Try Again" button
            paint.setColor(Color.WHITE);
            paint.setTextSize(80);
            int tryAgainX = AppConstants.SCREEN_WIDTH / 2 - 150;
            int tryAgainY = AppConstants.SCREEN_HEIGHT / 2 + 100;
            canvas.drawRect(tryAgainX - 20, tryAgainY - 80, tryAgainX + 280, tryAgainY + 20, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("Try Again", tryAgainX, tryAgainY, paint);

            // Draw "Back" button
            paint.setColor(Color.WHITE);
            int backX = AppConstants.SCREEN_WIDTH / 2 - 150;
            int backY = AppConstants.SCREEN_HEIGHT / 2 + 250;
            canvas.drawRect(backX - 20, backY - 80, backX + 280, backY + 20, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("Back", backX, backY, paint);

            // Define button areas (for touch detection)
            tryAgainButtonRect = new Rect(tryAgainX - 20, tryAgainY - 80, tryAgainX + 280, tryAgainY + 20);
            backButtonRect = new Rect(backX - 20, backY - 80, backX + 280, backY + 20);
        }
    }


    public void resetGame() {
        pipes.clear(); // Remove all pipes
        bird = new bird(); // Reset bird to the initial state
        score = 0; // Reset score
        gameState = 0; // Go back to the initial game state
    }
}