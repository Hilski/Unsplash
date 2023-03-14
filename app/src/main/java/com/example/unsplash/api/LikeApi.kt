package com.example.unsplash.api

import com.example.unsplash.data.models.LikeIt
import com.example.unsplash.data.models.RemoteUser
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {
    @POST("photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") idPhoto: String,
        @Header("Authorization") token: String
    ): LikeIt
}