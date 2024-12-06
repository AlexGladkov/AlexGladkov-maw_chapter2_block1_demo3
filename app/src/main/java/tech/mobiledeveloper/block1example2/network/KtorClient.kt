package tech.mobiledeveloper.block1example2.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import okhttp3.mockwebserver.MockWebServer
import tech.mobiledeveloper.block1example2.auth.AuthorizationHandler

class UnauthorizedException : Exception()
class RefreshUpdateFailedException : Exception()

class KtorClient(private val server: MockWebServer, private val authorizationHandler: AuthorizationHandler) {
    private val tokenHandler = TokenManager()

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
            if (e is RefreshUpdateFailedException) {
                authorizationHandler.logout()
            }
        }
    }
}