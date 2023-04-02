package com.example.unsplash.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "unsplashTable")
@Parcelize
data class UnsplashPhoto(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val likes: String,
    val liked_by_user: Boolean,
    val description: String?,
    @Embedded
    val exif: UnsplashExif?,
 //   @Embedded
  //  val location: UnsplashLocation?,
    @Embedded
    val urls: UnsplashPhotoPhotoUrls,
    @Embedded
    val user: UnsplashUser
) : Parcelable {
    @Serializable
    @Parcelize
    data class UnsplashExif(
        val make: String?,
        val model: String?,
        val exposure_time: String?,
        val aperture: String?,
        val focal_length: String?,
        val iso: String?
    ) : Parcelable
    @Serializable
    @Parcelize
    data class UnsplashLocation(
        val city: String?,
        val country: String?,
        val position: PhotoPosition?
    ) : Parcelable {
        @Serializable
        @Parcelize
        data class PhotoPosition(
            val latitude: Double?,
            val longitude: Double?
        ) : Parcelable
    }
    @Serializable
    @Parcelize
    data class UnsplashPhotoPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable
    @Serializable
    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_sourse=Mysplash&utm_medium=referral"
    }
}

