package com.example.archersflight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color

class AvatarAdapter(
    private val avatars: List<Avatar>,
    private val userCoins: Int,
    private val onAvatarClick: (Avatar) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>()  {

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
        holder.imageView.setImageResource(avatar.drawableRes)

        if (avatar.purchased) {
            holder.priceTextView.visibility = View.GONE
        } else {
            holder.priceTextView.visibility = View.VISIBLE
            holder.priceTextView.text = "${avatar.price} Coins"

            if (userCoins >= avatar.price) {
                holder.priceTextView.setTextColor(Color.GREEN)
            } else {
                holder.priceTextView.setTextColor(Color.RED)
            }
        }

        holder.itemView.setOnClickListener {
            if (!avatar.purchased) {
                onAvatarClick(avatar)
            }
        }
    }

    override fun getItemCount(): Int = avatars.size
}
