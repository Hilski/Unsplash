package com.example.unsplash.data.github.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteGithubUser(
    val id: Long,
    val login: String,
    val name: String,
)