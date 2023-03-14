package com.example.unsplash.api

import com.example.unsplash.data.models.RemoteUser
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
    @GET("me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): RemoteUser
}
