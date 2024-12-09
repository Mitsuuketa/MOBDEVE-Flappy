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
    private lateinit var highScoreManager: HighScoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
        AppConstants.initialization(applicationContext)
        musicServiceIntent = Intent(this, MusicService::class.java)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Initialize HighScoreManager
        highScoreManager = HighScoreManager(this)

        // Start the music service if music is enabled in SharedPreferences
        if (sharedPreferences.getBoolean(KEY_MUSIC, true)) {
            startService(musicServiceIntent)
        }
    }

    fun onGameOver(playerScore: Int) {
        // Retrieve the selected avatar from SharedPreferences
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val selectedAvatarResId = sharedPreferences.getInt("selected_avatar", R.drawable.default_avatar) // Default if none selected

        // Add the score and avatar to HighScoreManager
        highScoreManager.addScore(playerScore, selectedAvatarResId)
        println("Game Over! Player Score: $playerScore with Avatar: $selectedAvatarResId")

        // Navigate back to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("playerScore", playerScore)
        startActivity(intent)
        finish() // End the GameActivity
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

    private fun handleGameOver() {
        val highScoreManager = HighScoreManager(this)
        val playerScore = AppConstants.getGameEngine().getCurrentScore()

        // Retrieve the selected avatar from SharedPreferences
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val selectedAvatarResId = sharedPreferences.getInt("selected_avatar", R.drawable.default_avatar) // Default avatar if none is selected

        // Add the score and avatar to the high score manager
        highScoreManager.addScore(playerScore, selectedAvatarResId)
        println("Game Over! Player Score: $playerScore with Avatar: $selectedAvatarResId")

        // Navigate to the high score screen or show game over dialog (if applicable)
    }

}
