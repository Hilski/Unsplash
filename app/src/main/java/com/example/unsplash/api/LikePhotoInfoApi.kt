package com.example.unsplash.api

import com.example.unsplash.data.models.LikeIt
import com.example.unsplash.data.models.LikePhotoInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LikePhotoInfoApi {
    @GET("photos/{id}")
    suspend fun likePhotoInfo(
        @Path("id") idPhoto: String,
        @Header("Authorization") token: String
    ): LikePhotoInfo
}