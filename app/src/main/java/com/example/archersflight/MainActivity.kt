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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_activity)

        AppConstants.initialization(this.applicationContext)

        playBtn = findViewById(R.id.playBtn)
        // Purchase button logic
        playBtn.setOnClickListener{
            //Toast.makeText(this@MainActivity, "Play!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(intent)
        }
    }
}