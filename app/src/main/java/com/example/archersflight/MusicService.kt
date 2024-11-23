package com.example.archersflight

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true  // Set looping to true for continuous music
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()  // Start music if not already playing
        }
        return START_STICKY  // Keep the service running in the background
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null  // No binding needed
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()  // Stop music when service is destroyed
        }
        mediaPlayer.release()  // Release the media player resources
    }
}
