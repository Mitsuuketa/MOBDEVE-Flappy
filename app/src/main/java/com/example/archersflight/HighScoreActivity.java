package com.example.archersflight;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HighScoreActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Get the ListView
        ListView highScoreListView = findViewById(R.id.highScoreListView);

        // Get the high scores
        HighScoreManager highScoreManager = new HighScoreManager(this);
        List<HighScore> highScores = highScoreManager.getHighScoresWithAvatars();

        // Set the adapter
        HighScoreAdapter adapter = new HighScoreAdapter(this, highScores);
        highScoreListView.setAdapter(adapter);

        // Set the close button to finish the activity
        findViewById(R.id.closeButton).setOnClickListener(v -> finish());
    }
}
