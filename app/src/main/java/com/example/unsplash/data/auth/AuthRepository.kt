package com.example.unsplash.data.auth

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import timber.log.Timber

class AuthRepository {

    fun corruptAccessToken() {
        TokenStorage.accessToken = "fake token"
    }

    fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.tokenType = null
        TokenStorage.scope = null
        TokenStorage.createdAt = null

    }

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthRequest()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AppAuth.getEndSessionRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        //обмен кода на токен произошел успешно, сохраняем токены и завершаем авторизацию
        TokenStorage.accessToken = tokens.access_token
        TokenStorage.tokenType = tokens.token_type
        TokenStorage.scope = tokens.scope
        TokenStorage.createdAt = tokens.created_at

        Timber.tag("Oauth").d("6. Tokens accepted:\n access=${tokens.access_token}\nrefresh=${tokens.token_type}\nidToken=${tokens.scope}")
    }
}