package com.example.unsplash.data.models

data class LikeIt(
    val photo: LikedPhoto
) {
    data class LikedPhoto(
        val id: String,
        val liked_by_user: Boolean
    )
}