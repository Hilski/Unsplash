package com.example.unsplash.data.network

import com.example.unsplash.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    val interceptor = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val userApi: UserApi = retrofit.create(UserApi::class.java)

    val likeApi: LikeApi = retrofit.create(LikeApi::class.java)

    val UnlikeApi: UnLikeApi = retrofit.create(UnLikeApi::class.java)

    val likePhotoInfoApi: LikePhotoInfoApi = retrofit.create(LikePhotoInfoApi::class.java)

}