package com.example.unsplash.api

import com.example.unsplash.data.models.LikeIt
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface UnLikeApi {
    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(
        @Path("id") idPhoto: String,
        @Header("Authorization") token: String
    ): LikeIt
}