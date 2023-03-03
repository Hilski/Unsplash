package com.example.unsplash

import android.app.Application
import com.example.unsplash.data.network.Networking
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Networking.init(this)
        Timber.plant(Timber.DebugTree())
    }
}