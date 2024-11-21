package com.example.archersflight

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.widget.Button
import android.widget.ImageButton

class MainActivity : ComponentActivity() {
    private lateinit var playBtn: ImageButton
    private lateinit var highscoreBtn: ImageButton
    private lateinit var settingsBtn: ImageButton
    private lateinit var shopBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)

        AppConstants.initialization(this.applicationContext)

        playBtn = findViewById(R.id.playBtn)
        highscoreBtn = findViewById(R.id.highscoreBtn)
        settingsBtn = findViewById(R.id.settingsBtn)
        shopBtn = findViewById(R.id.shopBtn)
        
        // Purchase button logic
        playBtn.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Play!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        settingsBtn.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Settings!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        shopBtn.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Shop!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
        highscoreBtn.setOnClickListener{
            //Toast.makeText(this@MainActivity, "High-score!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
    }
}