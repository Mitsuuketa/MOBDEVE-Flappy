package com.example.archersflight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HighScoreAdapter extends ArrayAdapter<HighScore> {
    public HighScoreAdapter(Context context, List<HighScore> highScores) {
        super(context, 0, highScores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the custom layout for each item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_highscore, parent, false);
        }

        // Get the current HighScore object
        HighScore highScore = getItem(position);

        // Set the avatar
        ImageView avatarImageView = convertView.findViewById(R.id.avatarImageView);
        avatarImageView.setImageResource(highScore.getAvatarResId());

        // Set the score
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);
        scoreTextView.setText(String.format("Score: %d", highScore.getScore()));

        return convertView;
    }
}
