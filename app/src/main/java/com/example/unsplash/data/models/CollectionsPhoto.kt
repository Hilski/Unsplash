package com.example.unsplash.data.models

data class CollectionsPhoto(
    val cover_photo: CoverPhoto,
    val description: String?,
    val id: String,
    val links: Links,
    val title: String,
    val total_photos: Int,
    val user: User
) {

    data class User(
        val username: String
    )

    data class Links(
        val html: String,
        val photos: String,
        val related: String,
        val self: String
    )

    data class CoverPhoto(
        val urls: Urls,
        val liked_by_user: Boolean,
        val likes: Int
    ) {

        data class Urls(
            val full: String,
            val raw: String,
            val regular: String,
            val small: String,
            val small_s3: String,
            val thumb: String
        )
    }
}