package com.example.unsplash.data.auth

import android.net.Uri
import androidx.core.net.toUri
import com.example.unsplash.data.auth.models.TokensModel
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AppAuth {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null, // registration endpoint
        Uri.parse(AuthConfig.END_SESSION_URI)
    )

    fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = AuthConfig.CALLBACK_URL.toUri()

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setScopes(AuthConfig.SCOPE)
            .setRefreshToken(refreshToken)
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    response != null -> {
                        //получение токена произошло успешно
                        val tokens = TokensModel(
                            access_token = response.accessToken.orEmpty(),
                            token_type = response.refreshToken.orEmpty(),
                            scope = response.idToken.orEmpty(),
                            created_at = response.scope.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    //получение токенов произошло неуспешно, показываем ошибку
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }
                    else -> error("unreachable")
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    private object AuthConfig {
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val END_SESSION_URI = "https://unsplash.com/logout"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPE =
            "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"

        const val CLIENT_ID = "tFCb1X4iR4IkQYI4oTBBCVAsxivwhdVMaZMTIcJF9jw"
        const val CLIENT_SECRET = "ms3dV0_aeYFr7a0ZbT-zmgu92ptMtx7fze9YE1Tn5PU"
        const val CALLBACK_URL = "ru.kts.oauth://mysplash.com/callback"
        const val LOGOUT_CALLBACK_URL = "ru.kts.oauth://mysplash.com/logout_callback"
    }
}