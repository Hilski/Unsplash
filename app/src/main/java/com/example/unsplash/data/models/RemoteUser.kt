package com.example.unsplash.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteUser(
    val id: String,
    val username: String,
    val email: String,
    val total_likes: String,
    val links: Links
) {
    data class Links(
        val likes: String
    )
}