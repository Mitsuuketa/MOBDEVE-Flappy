package com.example.archersflight

import android.os.Bundle
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
    }
}
