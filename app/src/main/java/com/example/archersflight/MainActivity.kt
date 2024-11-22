package com.example.archersflight

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : ComponentActivity() {
    private lateinit var playBtn: ImageButton
    private lateinit var highscoreBtn: ImageButton
    private lateinit var settingsBtn: ImageButton
    private lateinit var shopBtn: ImageButton
    private lateinit var coinsTextView: TextView

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

        updateCoinsDisplay()

        playBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        settingsBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Settings!", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@MainActivity, GameActivity::class.java)
//            startActivity(intent)
        }
        shopBtn.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        highscoreBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "High-score!", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@MainActivity, GameActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun updateCoinsDisplay() {
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val userCoins = sharedPreferences.getInt("coins", 0)
        coinsTextView.text = "Coins: $userCoins"
    }
}