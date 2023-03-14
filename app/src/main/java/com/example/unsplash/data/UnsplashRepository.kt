package com.example.unsplash.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.data.auth.TokenStorage
import com.example.unsplash.data.models.LikeIt
import com.example.unsplash.data.models.LikePhotoInfo
import com.example.unsplash.data.models.RemoteUser
import com.example.unsplash.data.models.UnsplashResponse
import com.example.unsplash.data.network.RetrofitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
    private val token = "Bearer ${TokenStorage.accessToken}"

    suspend fun unsplashResponse(query: String, position: Int, params: Int): UnsplashResponse {
        return unsplashApi.searchPhotos(query, position, params, token)
    }

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query, UnsplashRepository(unsplashApi)) }
        ).liveData

    suspend fun getUserInformation(): RemoteUser {

        return RetrofitInstance.userApi.getCurrentUser(token)
    }

    suspend fun likePhotoInfo(idPhoto: String): LikePhotoInfo {
        return RetrofitInstance.likePhotoInfoApi.likePhotoInfo(idPhoto, token)
    }

    suspend fun likePhoto(idPhoto: String): LikeIt {
        return RetrofitInstance.likeApi.likePhoto(idPhoto, token)
    }

    suspend fun unlikePhoto(idPhoto: String): LikeIt {
        return RetrofitInstance.UnlikeApi.UnlikePhoto(idPhoto, token)
    }
}