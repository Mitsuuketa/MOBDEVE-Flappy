package com.example.archersflight

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {

    private lateinit var gameView: GameView
    private lateinit var musicServiceIntent: Intent
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "settings_prefs"
    private val KEY_MUSIC = "music_enabled"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
        AppConstants.initialization(applicationContext)
        musicServiceIntent = Intent(this, MusicService::class.java)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Start the music service if music is enabled in SharedPreferences
        if (sharedPreferences.getBoolean(KEY_MUSIC, true)) {
            startService(musicServiceIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure the music service continues running if music is enabled
        if (sharedPreferences.getBoolean(KEY_MUSIC, true)) {
            startService(musicServiceIntent)
        }
    }

    override fun onPause() {
        super.onPause()
        // Don't stop the music when switching to MainActivity unless the user disables it
    }

    override fun onDestroy() {
        super.onDestroy()
        // Optionally stop service if necessary
        stopService(musicServiceIntent)  // Only stop if music is disabled
        if (gameView != null) {
            gameView.surfaceDestroyed(gameView.getHolder());
        }
    }
}
