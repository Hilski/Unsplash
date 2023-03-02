package com.example.unsplash.api

import com.example.unsplash.data.UnsplashPhoto

data class UnsplashResponse (
    val results: List<UnsplashPhoto>
)