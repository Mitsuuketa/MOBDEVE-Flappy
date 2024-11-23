package com.example.archersflight

data class Avatar(
    val drawableRes: Int,
    val price: Int,
    var purchased: Boolean = false
)