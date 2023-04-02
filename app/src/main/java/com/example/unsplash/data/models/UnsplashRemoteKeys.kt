package com.example.unsplash.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsplashTableRemoteKeys")
data class UnsplashRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)