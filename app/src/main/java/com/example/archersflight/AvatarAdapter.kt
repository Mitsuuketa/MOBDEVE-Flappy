package com.example.archersflight

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.content.Context

class AvatarAdapter(
    private val avatars: List<Avatar>,
    private val userCoins: Int,
    private val onAvatarClick: (Avatar) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.avatarImageView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatar = avatars[position]
        val context = holder.itemView.context

        // Load the selected avatar from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        val selectedAvatarRes = sharedPreferences.getInt("selected_avatar", -1)

        // Load the optimized bitmap
        val optimizedBitmap = decodeSampledBitmapFromResource(
            context.resources,
            avatar.drawableRes,
            100,
            100
        )
        holder.imageView.setImageBitmap(optimizedBitmap)

        // Highlight if this is the selected avatar
        if (avatar.drawableRes == selectedAvatarRes) {
            holder.itemView.setBackgroundColor(Color.YELLOW) // Highlight selected avatar
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT) // Default background
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            onAvatarClick(avatar)
        }

        // Hide or show price based on purchase status
        if (avatar.purchased) {
            holder.priceTextView.visibility = View.GONE
        } else {
            holder.priceTextView.visibility = View.VISIBLE
            holder.priceTextView.text = "${avatar.price} Coins"

            holder.priceTextView.setTextColor(
                if (userCoins >= avatar.price) Color.GREEN else Color.RED
            )
        }
    }


    override fun getItemCount(): Int = avatars.size

    /**
     * Decodes a bitmap efficiently to reduce memory usage.
     */
    private fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // Load only the bounds of the image
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate sample size
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode the actual bitmap
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    /**
     * Calculates the sample size for downscaling the bitmap.
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
