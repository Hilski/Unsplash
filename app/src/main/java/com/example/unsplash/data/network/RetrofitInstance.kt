package com.example.unsplash.data.network

import android.content.Context
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.data.auth.TokenStorage
import com.example.unsplash.data.github.UserApi
import dagger.Provides
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

object RetrofitInstance {

    val interceptor = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val userApi: UserApi = retrofit.create(UserApi::class.java)

}