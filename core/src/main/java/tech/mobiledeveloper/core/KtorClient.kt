package tech.mobiledeveloper.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import okhttp3.mockwebserver.MockWebServer
import tech.mobiledeveloper.auth_api.AuthorizationHandler
import tech.mobiledeveloper.auth_api.TokenManager

class RefreshUpdateFailedException : Exception()

class KtorClient(
    private val tokenHandler: TokenManager,
    private val server: MockWebServer,
    private val authorizationHandler: AuthorizationHandler
) {
    private val _client = HttpClient(OkHttp) {
        install(TokenAuthFeature) {
            tokenManager = tokenHandler
        }
    }

    suspend fun get(url: String): HttpResponse = _client.get(url)

    suspend fun makeExpiredRequest() {
        val mockUrl = server.url("/unauthorized-endpoint").toString()

        try {
            val response = _client.get(mockUrl)
        } catch (e: Exception) {
            when (e) {
                is RefreshUpdateFailedException -> authorizationHandler.logout()
                else -> println("Error handled $e")
            }
        }
    }
}