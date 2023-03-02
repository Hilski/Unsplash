package com.example.unsplash.data.github

import com.example.unsplash.data.github.models.RemoteGithubUser
import retrofit2.http.GET

interface GithubApi {
    @GET("user")
    suspend fun getCurrentUser(
    ): RemoteGithubUser
}
