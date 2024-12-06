package tech.mobiledeveloper.auth_api

interface TokenManager {
    suspend fun setAccessToken(token: String)
    suspend fun setRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearData()
}