package com.example.unsplash.data.github

import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.data.github.models.RemoteUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UserApi {
 //   @Headers("Accept-Version: v1", "Authorization: Client-ID ${UnsplashApi.CLIENT_ID}")
    @GET("me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): RemoteUser
}
