package com.example.unsplash.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)