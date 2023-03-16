package com.example.unsplash.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionsPhoto(
    val cover_photo: CoverPhoto,
    val description: String?,
    val id: String,
    val links: Links,
    val title: String,
    val total_photos: Int,
    val user: User
) : Parcelable {
    @Parcelize
    data class User(
        val username: String
    ) : Parcelable

    @Parcelize
    data class Links(
        val html: String,
        val photos: String,
        val related: String,
        val self: String
    ) : Parcelable

    @Parcelize
    data class CoverPhoto(
        val urls: Urls,
        val liked_by_user: Boolean,
        val likes: Int
    ) : Parcelable {
        @Parcelize
        data class Urls(
            val full: String,
            val raw: String,
            val regular: String,
            val small: String,
            val small_s3: String,
            val thumb: String
        ) : Parcelable
    }
}