package com.example.unsplash.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val likes: String,
    val liked_by_user: Boolean,
    val description: String?,
    val exif: UnsplashExif?,
    val urls: UnsplashPhotoPhotoUrls,
    val user: UnsplashUser
) : Parcelable {
    @Parcelize
    data class UnsplashExif(
        val make: String?,
        val model: String?,
        val exposure_time: String?,
        val aperture: String?,
        val focal_length: String?,
        val iso: String?
    ) : Parcelable

    @Parcelize
    data class UnsplashPhotoPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_sourse=ImageSearchApp&utm_medium=referral"
    }
}

