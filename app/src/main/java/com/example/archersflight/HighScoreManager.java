package com.example.archersflight;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String PREF_NAME = "high_scores";
    private static final String SCORES_KEY = "scores";
    private static final String AVATARS_KEY = "avatars";
    private SharedPreferences sharedPreferences;

    public HighScoreManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Add a new score and avatar
    public void addScore(int score, int avatarResId) {
        List<HighScore> highScores = getHighScoresWithAvatars();

        // Check if the score already exists in the high scores list
        for (HighScore existingScore : highScores) {
            if (existingScore.getScore() == score) {
                return;
            }
        }

        // Add the new high score if it's not already in the list
        highScores.add(new HighScore(score, avatarResId));

        // Sort scores in descending order
        Collections.sort(highScores, (a, b) -> b.getScore() - a.getScore());

        // Limit to top 10 scores
        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }

        // Save updated high scores and avatars
        saveHighScores(highScores);
    }


    // Get the list of high scores with avatars
    public List<HighScore> getHighScoresWithAvatars() {
        String scoresString = sharedPreferences.getString(SCORES_KEY, "");
        String avatarsString = sharedPreferences.getString(AVATARS_KEY, "");

        List<HighScore> highScores = new ArrayList<>();
        if (!scoresString.isEmpty() && !avatarsString.isEmpty()) {
            String[] scoresArray = scoresString.split(",");
            String[] avatarsArray = avatarsString.split(",");

            for (int i = 0; i < scoresArray.length; i++) {
                try {
                    int score = Integer.parseInt(scoresArray[i].trim());
                    int avatarResId = Integer.parseInt(avatarsArray[i].trim());
                    highScores.add(new HighScore(score, avatarResId));
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Log any parsing errors
                }
            }
        }
        return highScores;
    }

    // Save high scores and avatars to SharedPreferences
    private void saveHighScores(List<HighScore> highScores) {
        StringBuilder scoresBuilder = new StringBuilder();
        StringBuilder avatarsBuilder = new StringBuilder();

        for (HighScore highScore : highScores) {
            scoresBuilder.append(highScore.getScore()).append(",");
            avatarsBuilder.append(highScore.getAvatarResId()).append(",");
        }

        // Remove trailing commas
        if (scoresBuilder.length() > 0) scoresBuilder.setLength(scoresBuilder.length() - 1);
        if (avatarsBuilder.length() > 0) avatarsBuilder.setLength(avatarsBuilder.length() - 1);

        // Save to SharedPreferences
        sharedPreferences.edit()
                .putString(SCORES_KEY, scoresBuilder.toString())
                .putString(AVATARS_KEY, avatarsBuilder.toString())
                .apply();
    }
}
