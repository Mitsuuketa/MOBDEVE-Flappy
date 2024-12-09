package com.example.archersflight

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.activity.compose.setContent
import android.view.View
import android.view.LayoutInflater
import android.app.ActivityManager
class MainActivity : ComponentActivity() {
    private lateinit var playBtn: ImageButton
    private lateinit var highscoreBtn: ImageButton
    private lateinit var settingsBtn: ImageButton
    private lateinit var shopBtn: ImageButton
    private lateinit var coinsTextView: TextView


    //settings
    private lateinit var overlayLayout: FrameLayout
    private lateinit var musicSwitch: Switch
    private lateinit var closeBtn: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "settings_prefs"
    private val KEY_MUSIC = "music_enabled"
    private lateinit var musicServiceIntent: Intent
    private lateinit var gameView: GameView // Declare gameView as a lateinit variable

    private lateinit var tapEffectSwitch: Switch
    private val KEY_TAP_EFFECT = "tap_effect_enabled"

    private val highScoreManager by lazy { HighScoreManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)

        AppConstants.initialization(this.applicationContext)

        playBtn = findViewById(R.id.playBtn)
        highscoreBtn = findViewById(R.id.highscoreBtn)
        settingsBtn = findViewById(R.id.settingsBtn)
        shopBtn = findViewById(R.id.shopBtn)
        coinsTextView = findViewById(R.id.coinsTextView)


        // music
        musicServiceIntent = Intent(this, MusicService::class.java)
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Check if music is enabled in SharedPreferences
        val isMusicEnabled = sharedPreferences.getBoolean(KEY_MUSIC, true)
        if (isMusicEnabled) {
            startService(musicServiceIntent) // Start music if enabled
        }

        val lastScore = intent.getIntExtra("playerScore", -1)
        if (lastScore != -1) {
            println("Last game score: $lastScore") // Debug log
            Toast.makeText(this, "Last Score: $lastScore", Toast.LENGTH_SHORT).show()
        }

        //settings
        // Find the settings button and overlay container in the layout
        settingsBtn = findViewById(R.id.settingsBtn)
        overlayLayout = findViewById(R.id.settings_overlay)  // The container for the overlay
        overlayLayout.isVisible = false // Initially, the overlay is hidden

        updateCoinsDisplay()

        playBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }

        shopBtn.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        highscoreBtn.setOnClickListener {
            val intent = Intent(this, HighScoreActivity::class.java)
            startActivity(intent)
        }
        settingsBtn.setOnClickListener{
            showSettingsOverlay()
        }
    }

    private fun updateCoinsDisplay() {
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val userCoins = sharedPreferences.getInt("coins", 0)
        coinsTextView.text = "Coins: $userCoins"
    }


    // Function to show the settings overlay
    private fun showSettingsOverlay() {
        val inflater = LayoutInflater.from(this)
        val overlayView = inflater.inflate(R.layout.overlay_settings, null)
        overlayLayout.addView(overlayView)
        overlayLayout.isVisible = true

        musicSwitch = overlayView.findViewById(R.id.musicSwitch)
        tapEffectSwitch = overlayView.findViewById(R.id.tapEffectSwitch)
        closeBtn = overlayView.findViewById(R.id.closeBtn)

        // Retrieve saved states
        val isMusicEnabled = sharedPreferences.getBoolean(KEY_MUSIC, true)
        val isTapEffectEnabled = sharedPreferences.getBoolean(KEY_TAP_EFFECT, true)

        // Set initial states
        musicSwitch.isChecked = isMusicEnabled
        tapEffectSwitch.isChecked = isTapEffectEnabled

        // Music Switch Listener
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(KEY_MUSIC, isChecked).apply()
            if (isChecked) {
                startService(Intent(this, MusicService::class.java)) // Start music
            } else {
                stopService(Intent(this, MusicService::class.java)) // Stop music
            }
        }

        // Tap Effect Switch Listener
        tapEffectSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(KEY_TAP_EFFECT, isChecked).apply()
        }

        // Close Button Listener
        closeBtn.setOnClickListener {
            overlayLayout.isVisible = false
        }
    }


    // Save the music setting to SharedPreferences
    private fun saveMusicSetting(isMusicEnabled: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_MUSIC, isMusicEnabled)
        editor.apply()  // Save the setting
    }

    override fun onResume() {
        super.onResume()
        val isMusicEnabled = sharedPreferences.getBoolean(KEY_MUSIC, true)
        if (isMusicEnabled) {
            startService(musicServiceIntent) // Resume music if enabled
        }
        updateCoinsDisplay()
    }

    override fun onPause() {
        super.onPause()
        // Stop the music service when the app is minimized
        stopService(musicServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the music service only when the last activity is closed
        stopService(musicServiceIntent)
        AppConstants.releaseResources(); // Release MediaPlayer resources
    }

    private fun isMusicServiceRunning(): Boolean {
        // Check if the MusicService is running
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (MusicService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }



}