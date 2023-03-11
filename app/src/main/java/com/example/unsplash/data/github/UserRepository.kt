package com.example.unsplash.data.github

import com.example.unsplash.data.auth.TokenStorage.accessToken
import com.example.unsplash.data.github.models.RemoteUser
import com.example.unsplash.data.network.RetrofitInstance
import okhttp3.OkHttpClient


class UserRepository {
    suspend fun getUserInformation(): RemoteUser {
        val token: String = "Bearer $accessToken" ?: ""
        return RetrofitInstance.userApi.getCurrentUser(token)
    }
}