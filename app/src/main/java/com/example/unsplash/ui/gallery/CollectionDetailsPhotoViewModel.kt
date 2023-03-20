package com.example.unsplash.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplash.R
import com.example.unsplash.data.UnsplashRepository
import com.example.unsplash.data.models.CollectionDetailsPhoto
import com.example.unsplash.data.models.LikeIt
import com.example.unsplash.data.models.LikePhotoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsPhotoViewModel @Inject constructor(private val repository: UnsplashRepository) :
    ViewModel() {
    private val likeItMutableStateFlow = MutableStateFlow<LikeIt?>(null)
    private val likeItInfoMutableStateFlow = MutableStateFlow<LikePhotoInfo?>(null)
    private val getPhotoMutableStateFlow = MutableStateFlow<CollectionDetailsPhoto?>(null)
    private val loadingMutableStateFlow = MutableStateFlow(false)
    private val toastEventChannel = Channel<Int>(Channel.BUFFERED)

    val likeItFlow: Flow<LikeIt?>
        get() = likeItMutableStateFlow.asStateFlow()
    val likeItInfoFlow: Flow<LikePhotoInfo?>
        get() = likeItInfoMutableStateFlow.asStateFlow()
    val getPhotoFlow: Flow<CollectionDetailsPhoto?>
        get() = getPhotoMutableStateFlow.asStateFlow()

    val toastFlow: Flow<Int>
        get() = toastEventChannel.receiveAsFlow()

    fun getPhoto(idPhoto: String) {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                repository.getCollectionDetailsPhoto(idPhoto)
            }.onSuccess {
                getPhotoMutableStateFlow.value = it
                loadingMutableStateFlow.value = false
            }.onFailure {
                loadingMutableStateFlow.value = false
                getPhotoMutableStateFlow.value = null
                toastEventChannel.trySendBlocking(R.string.get_photo_error)
            }
        }
    }

    fun likePhotoInfo(idPhoto: String) {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                repository.likePhotoInfo(idPhoto)
            }.onSuccess {
                likeItInfoMutableStateFlow.value = it
                loadingMutableStateFlow.value = false
            }.onFailure {
                loadingMutableStateFlow.value = false
                likeItInfoMutableStateFlow.value = null
                toastEventChannel.trySendBlocking(R.string.like_info_error)
            }
        }
    }

    fun likePhoto(idPhoto: String) {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                repository.likePhoto(idPhoto)
            }.onSuccess {
                likeItMutableStateFlow.value = it
                loadingMutableStateFlow.value = false
                toastEventChannel.trySendBlocking(R.string.you_like_it)
            }.onFailure {
                loadingMutableStateFlow.value = false
                likeItMutableStateFlow.value = null
                toastEventChannel.trySendBlocking(R.string.like_error)
            }
        }
    }

    fun unlikePhoto(idPhoto: String) {
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                repository.unlikePhoto(idPhoto)
            }.onSuccess {
                likeItMutableStateFlow.value = it
                loadingMutableStateFlow.value = false
                toastEventChannel.trySendBlocking(R.string.you_un_like_it)
            }.onFailure {
                loadingMutableStateFlow.value = false
                likeItMutableStateFlow.value = null
                toastEventChannel.trySendBlocking(R.string.unlike_error)
            }
        }
    }
}