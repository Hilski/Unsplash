package com.example.unsplash.ui.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unsplash.data.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyLikedPhotosViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
) :
    ViewModel() {
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
    val collections = currentQuery.switchMap { queryString ->
        repository.getSearchMyLikedPhotosResults(queryString).cachedIn(viewModelScope)
    }

    fun myLikedPhotoUsername(username: String) {
        currentQuery.value = username
    }

    companion object {
        private const val CURRENT_QUERY = ""
        private const val DEFAULT_QUERY = ""
    }
}