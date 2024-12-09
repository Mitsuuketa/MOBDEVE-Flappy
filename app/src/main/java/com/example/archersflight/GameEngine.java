package com.example.archersflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameEngine {
    private Context context;
    BackgroundImage backgroundImage;
    bird bird;

    static int gameState;

    private List<Pipe> pipes; // List to manage pipes
    private int pipeSpawnTime = 100; // Time between pipe spawns (frames)
    private int pipeTimer = 0;
    int score = 0;

    Rect tryAgainButtonRect;
    Rect backButtonRect;

    public GameEngine(Context context) {
        this.context = context;
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

            if (bird.getY() >= AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getBirdHeight()) {
                handleGameOver();
                bird.setY(AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getBirdHeight());
            }

            int currentFrame = bird.getCurrentFrame();
            Bitmap birdBitmap = AppConstants.getBitmapBank().getBird(currentFrame);

            // Rotate the bird bitmap based on velocity
            Matrix matrix = new Matrix();
            float rotationAngle = bird.getVelocity() > 0 ? Math.min(bird.getVelocity() * 2, 30) : -20; // Cap rotation angle
            matrix.postRotate(rotationAngle, birdBitmap.getWidth() / 2.0f, birdBitmap.getHeight() / 2.0f);
            matrix.postTranslate(bird.getX(), bird.getY());

            canvas.drawBitmap(birdBitmap, matrix, null);

            currentFrame++;
            if (currentFrame > bird.maxFrame) {
                currentFrame = 0;
            }
            bird.setCurrentFrame(currentFrame);
        }
    }


    public void onTap() {
        if (gameState == 1) {  // Only allow jumping if the game is ongoing
            bird.setVelocity(AppConstants.VELOCITY_WHEN_JUMPED);  // Apply an upward velocity on tap
        }
    }

    public void updateAndDrawObstacles(Canvas canvas) {
        Paint paint = new Paint();

        if (gameState == 1) {
            if (pipeTimer == 0) {
                pipes.add(new Pipe(AppConstants.SCREEN_HEIGHT, getContext().getResources())); // Pass resources here
                pipeTimer = pipeSpawnTime;
            } else {
                pipeTimer--;
            }

            Iterator<Pipe> iterator = pipes.iterator();
            while (iterator.hasNext()) {
                Pipe pipe = iterator.next();
                pipe.update();

                if (pipe.isOffScreen()) {
                    iterator.remove();
                }
            }

            // Draw pipes and check for collisions
            for (Pipe pipe : pipes) {
                pipe.draw(canvas);
                if (!pipe.isScored() && pipe.getX() + Pipe.getPipeWidth() < bird.getX()) {
                    score++;
                    pipe.setScored(true);

                    // Add coins to the user's account when score increases
                    updateCoins(1);
                }

                int birdWidth = AppConstants.getBitmapBank().getBirdWidth();
                int birdHeight = AppConstants.getBitmapBank().getBirdHeight();

                // Check for collision
                Rect birdRect = new Rect(
                        bird.getX(),
                        bird.getY(),
                        bird.getX() + birdWidth,
                        bird.getY() + birdHeight
                );

                if (Rect.intersects(birdRect, pipe.getTopPipe()) || Rect.intersects(birdRect, pipe.getBottomPipe())) {
                    handleGameOver(); // Game over state
                }
            }

            // Draw the current score during the game
            paint.setColor(Color.BLACK); // Set the text color to black
            paint.setTextSize(100); // Set the text size
            // Calculate the width of the text
            float textWidth = paint.measureText("" + score);

            // Calculate the x position to center the text
            float xPos = (AppConstants.SCREEN_WIDTH - textWidth) / 2;

            // Set the y position for the text (you can adjust this to position it where you want)
            float yPos = 100; // You can change this value as needed

            // Draw the score text on the canvas
            canvas.drawText("" + score, xPos, yPos, paint);

        } else if (gameState == 2) {
            // Retrieve the high scores
            HighScoreManager highScoreManager = new HighScoreManager(context);
            List<HighScore> highScores = highScoreManager.getHighScoresWithAvatars();
            int highScore = highScores.isEmpty() ? 0 : highScores.get(0).getScore();

            // Draw "Game Over" message
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Game Over!", AppConstants.SCREEN_WIDTH / 2f, AppConstants.SCREEN_HEIGHT / 2f - 200, paint);

            // Display Current Score
            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            canvas.drawText("Score: " + score, AppConstants.SCREEN_WIDTH / 2f, AppConstants.SCREEN_HEIGHT / 2f - 100, paint);

            // Display High Score
            canvas.drawText("High Score: " + highScore, AppConstants.SCREEN_WIDTH / 2f, AppConstants.SCREEN_HEIGHT / 2f, paint);

            // Draw "Try Again" button
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            int tryAgainX = AppConstants.SCREEN_WIDTH / 2 - 150;
            int tryAgainY = AppConstants.SCREEN_HEIGHT / 2 + 100;
            RectF tryAgainRect = new RectF(tryAgainX - 20, tryAgainY - 80, tryAgainX + 280, tryAgainY + 20);
            canvas.drawRoundRect(tryAgainRect, 30, 30, paint); // Rounded rectangle with 30px corner radius

            // Draw centered text for "Try Again"
            paint.setColor(Color.BLACK);
            paint.setTextSize(80);
            float tryAgainTextX = tryAgainRect.centerX();
            float tryAgainTextY = tryAgainRect.centerY() - ((paint.descent() + paint.ascent()) / 2); // Adjust text vertically
            canvas.drawText("Play", tryAgainTextX, tryAgainTextY, paint);

            // Draw "Back" button
            paint.setColor(Color.WHITE);
            int backX = AppConstants.SCREEN_WIDTH / 2 - 150;
            int backY = AppConstants.SCREEN_HEIGHT / 2 + 250;
            RectF backRect = new RectF(backX - 20, backY - 80, backX + 280, backY + 20);
            canvas.drawRoundRect(backRect, 30, 30, paint); // Rounded rectangle with 30px corner radius

            // Draw centered text for "Back"
            paint.setColor(Color.BLACK);
            float backTextX = backRect.centerX();
            float backTextY = backRect.centerY() - ((paint.descent() + paint.ascent()) / 2); // Adjust text vertically
            canvas.drawText("Menu", backTextX, backTextY, paint);

            // Define button areas (for touch detection)
            tryAgainButtonRect = new Rect((int) tryAgainRect.left, (int) tryAgainRect.top, (int) tryAgainRect.right, (int) tryAgainRect.bottom);
            backButtonRect = new Rect((int) backRect.left, (int) backRect.top, (int) backRect.right, (int) backRect.bottom);
        }
    }

    /**
     * Handles game over logic, updates high scores, and resets state.
     */

    public int getCurrentScore() {
        return score;
    }

    private void handleGameOver() {
        gameState = 2;
        Log.d("GameEngine", "Game Over! Final Score: " + score);

        // Get the avatar resource ID from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        int selectedAvatarResId = sharedPreferences.getInt("selected_avatar", R.drawable.default_avatar); // Default avatar if none selected

        // Update high scores with score and avatar
        HighScoreManager highScoreManager = new HighScoreManager(context);
        highScoreManager.addScore(getCurrentScore(), selectedAvatarResId);
        Log.d("GameEngine", "High score updated with score: " + score + ", avatar: " + selectedAvatarResId);
    }


    /**
     * Updates the user's coins in SharedPreferences based on the current score.
     */
    private void updateCoins(int score) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);

        // Calculate coins to add (each score is worth 500 coins)
        int additionalCoins = score * 10;

        // Get the current coin balance
        int currentCoins = sharedPreferences.getInt("coins", 0);

        // Update the coin balance
        sharedPreferences.edit().putInt("coins", currentCoins + additionalCoins).apply();
    }

    public Context getContext() {
        return context;
    }

    public void resetGame() {
        pipes.clear(); // Remove all pipes
        bird = new bird(); // Reset bird to the initial state
        score = 0; // Reset score
        gameState = 0; // Go back to the initial game state
    }
}
