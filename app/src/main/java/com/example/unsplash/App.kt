package com.example.unsplash

import android.app.Application
import com.example.unsplash.data.network.Networking
import timber.log.Timber

class GithubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Networking.init(this)
        Timber.plant(Timber.DebugTree())
    }
}