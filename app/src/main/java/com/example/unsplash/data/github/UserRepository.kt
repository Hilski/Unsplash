package com.example.unsplash.data.github

import com.example.unsplash.data.github.models.RemoteGithubUser
import com.example.unsplash.data.network.Networking


class UserRepository {
    suspend fun getUserInformation(): RemoteGithubUser {
        return Networking.githubApi.getCurrentUser()
    }
}