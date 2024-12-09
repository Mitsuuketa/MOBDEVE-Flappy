package com.example.archersflight;

public class HighScore {
    private int score;
    private int avatarResId;

    public HighScore(int score, int avatarResId) {
        this.score = score;
        this.avatarResId = avatarResId;
    }

    public int getScore() {
        return score;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
