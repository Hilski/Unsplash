package com.example.unsplash.api

import com.example.unsplash.BuildConfig
import com.example.unsplash.data.models.*

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    //   @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): UnsplashResponse

    @GET("search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): CollectionsResponse

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") idPhoto: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): List<GetCollectionsPhoto>

    @GET("photos/{id}")
    suspend fun getCollectionDetailsPhotos(
        @Path("id") idPhoto: String,
        @Header("Authorization") token: String
    ): CollectionDetailsPhoto

    @GET("users/{username}/likes")
    suspend fun getMyLikedPhotos(
        @Path("username") idPhoto: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): List<MyLikedPhoto>
}