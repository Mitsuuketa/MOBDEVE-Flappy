package com.example.archersflight

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShopActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var avatarAdapter: AvatarAdapter
    private lateinit var shopCoinsTextView: TextView
    private val avatarList = listOf(
        Avatar(R.drawable.avatar1, 100),
        Avatar(R.drawable.avatar2, 200),
        Avatar(R.drawable.avatar3, 300),
        Avatar(R.drawable.avatar4, 400),
        Avatar(R.drawable.avatar5, 500),
        Avatar(R.drawable.avatar6, 600)
    )
    private var userCoins = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("coins", 500).apply()
        userCoins = sharedPreferences.getInt("coins", 0)

        if(userCoins < 0) {
            userCoins = 0;
        }

        shopCoinsTextView = findViewById(R.id.shopCoinsTextView)
        updateCoinsDisplay()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        avatarAdapter = AvatarAdapter(avatarList, userCoins) { selectedAvatar ->
            onAvatarSelected(selectedAvatar)
        }
        recyclerView.adapter = avatarAdapter
    }

    private fun onAvatarSelected(avatar: Avatar) {
        if (userCoins >= avatar.price) {
            userCoins -= avatar.price
            avatar.purchased = true

            val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
            sharedPreferences.edit()
                .putInt("coins", userCoins)
                .putInt("selected_avatar", avatar.drawableRes)
                .apply()

            Toast.makeText(this, "Avatar purchased and selected!", Toast.LENGTH_SHORT).show()
            updateCoinsDisplay()

            recyclerView.adapter?.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCoinsDisplay() {
        shopCoinsTextView.text = "Coins: $userCoins"
    }
}
