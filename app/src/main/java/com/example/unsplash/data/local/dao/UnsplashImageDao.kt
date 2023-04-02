package com.example.unsplash.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplash.data.models.UnsplashPhoto

@Dao
interface UnsplashImageDao {

    @Query("SELECT * FROM unsplashTable")
    fun getAllImages(): PagingSource<Int, UnsplashPhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images: List<UnsplashPhoto>)

    @Query("DELETE FROM unsplashTable")
    suspend fun deleteAllImages()

}