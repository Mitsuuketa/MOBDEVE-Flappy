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

    // List of available avatars
    private val avatarList = listOf(
        Avatar(R.drawable.avatar1, 100),
        Avatar(R.drawable.avatar2, 200),
        Avatar(R.drawable.avatar3, 300),
        Avatar(R.drawable.avatar4, 400),
        Avatar(R.drawable.avatar5, 500),
        Avatar(R.drawable.avatar6, 600)
    )
    private var userCoins = 0
    private val sharedPreferencesName = "GamePrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        userCoins = sharedPreferences.getInt("coins", 500)

        // Load purchased avatars
        val purchasedSet = sharedPreferences.getStringSet("purchased_avatars", emptySet())
        avatarList.forEach { avatar ->
            avatar.purchased = purchasedSet?.contains(avatar.drawableRes.toString()) == true
        }

        shopCoinsTextView = findViewById(R.id.shopCoinsTextView)
        updateCoinsDisplay()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        avatarAdapter = AvatarAdapter(avatarList, userCoins) { selectedAvatar ->
            onAvatarSelected(selectedAvatar)
        }
        recyclerView.adapter = avatarAdapter
    }

    private fun onAvatarSelected(avatar: Avatar) {
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        val purchasedSet = sharedPreferences.getStringSet("purchased_avatars", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        if (!avatar.purchased) {
            // Handle purchasing the avatar
            if (userCoins >= avatar.price) {
                // Deduct coins and mark as purchased
                userCoins -= avatar.price
                avatar.purchased = true

                // Save updated coins
                sharedPreferences.edit().putInt("coins", userCoins).apply()

                // Add avatar to purchased list
                purchasedSet.add(avatar.drawableRes.toString())
                sharedPreferences.edit().putStringSet("purchased_avatars", purchasedSet).apply()

                // Set this avatar as selected
                sharedPreferences.edit().putInt("selected_avatar", avatar.drawableRes).apply()

                Toast.makeText(this, "Avatar purchased and selected!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not enough coins!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle switching to an already purchased avatar
            if (purchasedSet.contains(avatar.drawableRes.toString())) {
                sharedPreferences.edit().putInt("selected_avatar", avatar.drawableRes).apply()

                Toast.makeText(this, "Avatar selected!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: Avatar not marked as purchased!", Toast.LENGTH_SHORT).show()
            }
        }

        // Update coins display and refresh the RecyclerView
        updateCoinsDisplay()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateCoinsDisplay() {
        val sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE)
        userCoins = sharedPreferences.getInt("coins", 0)
        shopCoinsTextView.text = "Coins: $userCoins"
    }
}


