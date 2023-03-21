package com.example.unsplash.ui.user_info

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplash.R
import com.example.unsplash.data.UnsplashRepository
import com.example.unsplash.data.auth.AuthRepository
import com.example.unsplash.data.models.RemoteUser
import com.example.unsplash.data.network.RetrofitInstance.interceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    application: Application
) :
    AndroidViewModel(application) {

    private val authService: AuthorizationService = AuthorizationService(getApplication())
    private val authRepository = AuthRepository()
    private val loadingMutableStateFlow = MutableStateFlow(false)
    private val userInfoMutableStateFlow = MutableStateFlow<RemoteUser?>(null)
    private val toastEventChannel = Channel<Int>(Channel.BUFFERED)
    private val logoutPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val logoutCompletedEventChannel = Channel<Unit>(Channel.BUFFERED)

    val loadingFlow: Flow<Boolean>
        get() = loadingMutableStateFlow.asStateFlow()

    val userInfoFlow: Flow<RemoteUser?>
        get() = userInfoMutableStateFlow.asStateFlow()

    val toastFlow: Flow<Int>
        get() = toastEventChannel.receiveAsFlow()

    val logoutPageFlow: Flow<Intent>
        get() = logoutPageEventChannel.receiveAsFlow()

    val logoutCompletedFlow: Flow<Unit>
        get() = logoutCompletedEventChannel.receiveAsFlow()

    fun loadUserInfo() {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        viewModelScope.launch {
            loadingMutableStateFlow.value = true
            runCatching {
                repository.getUserInformation()
            }.onSuccess {
                userInfoMutableStateFlow.value = it
                loadingMutableStateFlow.value = false
            }.onFailure {
                loadingMutableStateFlow.value = false
                userInfoMutableStateFlow.value = null
                toastEventChannel.trySendBlocking(R.string.get_user_error)
            }
        }
    }

    fun logout() {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val logoutPageIntent = authService.getEndSessionRequestIntent(
            authRepository.getEndSessionRequest(),
            customTabsIntent
        )

        logoutPageEventChannel.trySendBlocking(logoutPageIntent)
    }

    fun webLogoutComplete() {
        authRepository.logout()
        logoutCompletedEventChannel.trySendBlocking(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}