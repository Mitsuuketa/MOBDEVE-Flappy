package com.mobdeve.yourname.exercise3lifecyclesp

class PostModel(
    val imageId: Int,
    val datePosted: String,
    val caption: String?,
    val location: String?,
    var liked: Boolean,
    val username: String,
    val userImageId: Int
)