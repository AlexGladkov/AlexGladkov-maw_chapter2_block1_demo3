package tech.mobiledeveloper.auth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.mobiledeveloper.auth_api.TokenManager

class TokenManagerImpl: TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = "first_refresh_token"
    private val mutex = Mutex()

    override suspend fun setAccessToken(token: String) {
        mutex.withLock {
            accessToken = token
        }
    }

    override suspend fun setRefreshToken(token: String) {
        mutex.withLock {
            refreshToken = token
        }
    }

    override suspend fun getRefreshToken(): String? {
        return mutex.withLock {
            refreshToken
        }
    }

    override suspend fun clearData() {
        mutex.withLock {
            accessToken = null
            refreshToken = null
        }
    }
}