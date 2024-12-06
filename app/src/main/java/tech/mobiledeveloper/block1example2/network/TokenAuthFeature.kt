package tech.mobiledeveloper.block1example2.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.sync.Mutex
import tech.mobiledeveloper.block1example2.auth.AuthorizationHandler

internal class TokenAuthFeature(
    private val tokenManager: TokenManager
) {
    private val refreshMutex = Mutex()

    class Config {
        lateinit var tokenManager: TokenManager
    }

    companion object Feature : HttpClientPlugin<Config, TokenAuthFeature> {
        override val key = AttributeKey<TokenAuthFeature>("TokenAuthFeature")

        override fun prepare(block: Config.() -> Unit): TokenAuthFeature {
            val config = Config().apply(block)
            return TokenAuthFeature(config.tokenManager)
        }

        @OptIn(InternalAPI::class)
        override fun install(plugin: TokenAuthFeature, scope: HttpClient) {
            scope.plugin(HttpSend).intercept { context ->
                val call = execute(context)
                val status =
                    call.response.status == HttpStatusCode.Unauthorized
                            || call.response.status == HttpStatusCode.Conflict

                if (status && !call.response.request.url.encodedPath.contains("https://ktor.io/")) {
                    try {
                        plugin.refreshMutex.lock()
                        val newToken = plugin.refreshToken(scope)

                        val request = HttpRequestBuilder().apply {
                            takeFromWithExecutionContext(context)
                            headers[HttpHeaders.Authorization] = "Bearer $newToken"
                        }

                        execute(request)
                    } finally {
                        plugin.refreshMutex.unlock()
                    }
                } else {
                    call
                }
            }
        }
    }

    private suspend fun refreshToken(httpClient: HttpClient): String {
        val refreshToken = tokenManager.getRefreshToken()

        if (refreshToken.isNullOrBlank()) {
            throw RefreshUpdateFailedException()
        }

        try {
            val response = httpClient.get("https://ktor.io/")

            if (response.status != HttpStatusCode.OK) {
                val authToken = "second_access_token"
                tokenManager.setAccessToken(authToken)
                tokenManager.setRefreshToken("second_refresh_token")

                return authToken
            } else {
                throw RefreshUpdateFailedException()
            }
        } catch (e: Exception) {
            throw RefreshUpdateFailedException()
        }
    }
}