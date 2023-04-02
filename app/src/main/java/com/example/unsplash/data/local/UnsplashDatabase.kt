package com.example.unsplash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplash.data.local.dao.UnsplashImageDao
import com.example.unsplash.data.local.dao.UnsplashRemoteKeysDao
import com.example.unsplash.data.models.UnsplashPhoto
import com.example.unsplash.data.models.UnsplashRemoteKeys

@Database(entities = [UnsplashPhoto::class, UnsplashRemoteKeys::class], version = 1)
abstract class UnsplashDatabase : RoomDatabase() {

    abstract fun unsplashImageDao(): UnsplashImageDao
    abstract fun unsplashRemoteKeysDao(): UnsplashRemoteKeysDao

}