package com.example.unsplash.data.github.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteUser(
    val id: String,
    val username: String,
    val location: String,
)