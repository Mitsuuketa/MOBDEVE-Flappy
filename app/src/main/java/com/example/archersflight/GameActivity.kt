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

class GameActivity : ComponentActivity() {

    private lateinit var gameView: GameView // Declare gameView as a lateinit variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize gameView
        gameView = GameView(this)
        setContentView(gameView)
    }
}