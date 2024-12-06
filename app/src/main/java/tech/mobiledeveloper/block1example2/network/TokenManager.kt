package tech.mobiledeveloper.block1example2.network

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = "first_refresh_token"
    private val mutex = Mutex()

    suspend fun setAccessToken(token: String) {
        mutex.withLock {
            accessToken = token
        }
    }

    suspend fun setRefreshToken(token: String) {
        mutex.withLock {
            refreshToken = token
        }
    }

    suspend fun getRefreshToken(): String? {
        return mutex.withLock {
            refreshToken
        }
    }

    suspend fun clearData() {
        mutex.withLock {
            accessToken = null
            refreshToken = null
        }
    }
}