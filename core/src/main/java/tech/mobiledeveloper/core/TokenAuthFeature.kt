package tech.mobiledeveloper.core

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
import tech.mobiledeveloper.auth_api.TokenManager
import java.util.concurrent.TimeoutException

class TokenAuthFeature(
    private val tokenManager: TokenManager
) {

    private val refreshMutex = Mutex()

    class Config {
        lateinit var tokenManager: TokenManager
    }

    companion object Feature : HttpClientPlugin<Config, TokenAuthFeature> {
        override val key: AttributeKey<TokenAuthFeature> = AttributeKey<TokenAuthFeature>("TokenAuthFeature")

        override fun prepare(block: Config.() -> Unit): TokenAuthFeature {
            val config = Config().apply(block)
            return TokenAuthFeature(tokenManager = config.tokenManager)
        }

        @OptIn(InternalAPI::class)
        override fun install(plugin: TokenAuthFeature, scope: HttpClient) {
            scope.plugin(HttpSend).intercept { context ->
                val call = execute(context)
                val acceptableStatus = call.response.status == HttpStatusCode.Unauthorized

                if (acceptableStatus && !call.response.request.url.encodedPath.contains("https://ktor.io/")) {
                    try {
                        plugin.refreshMutex.lock()
                        val newToken = plugin.refreshToken(scope)

                        val request = HttpRequestBuilder().apply {
                            takeFromWithExecutionContext(context)
                            headers[HttpHeaders.Authorization] = "Bearer $newToken"
                        }

                        execute(request)
                    } catch (e: Exception) {
                        when (e) {
                            is TimeoutException -> call
                            else -> throw RefreshUpdateFailedException()
                        }
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
            tokenManager.clearData()
            throw RefreshUpdateFailedException()
        }

        try {
            val response = httpClient.get("https://ktor.io/")

            if (response.status == HttpStatusCode.OK) {
                val accessToken = "second_access_token"
                tokenManager.setAccessToken(accessToken)
                tokenManager.setRefreshToken("second_refresh_token")

                return accessToken
            } else {
                tokenManager.clearData()
                throw RefreshUpdateFailedException()
            }
        } catch (e: Exception) {
            when (e) {
                is TimeoutException -> throw e
                else -> {
                    tokenManager.clearData()
                    throw RefreshUpdateFailedException()
                }
            }
        }
    }
}