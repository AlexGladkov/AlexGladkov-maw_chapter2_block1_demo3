package tech.mobiledeveloper.block1example2.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import okhttp3.mockwebserver.MockWebServer

class UnauthorizedException : Exception()

class KtorClient(private val server: MockWebServer) {
    private val _client = HttpClient(OkHttp) {
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status == HttpStatusCode.Unauthorized) {
                    throw UnauthorizedException()
                }
            }

            handleResponseExceptionWithRequest { exception, _ ->

            }
        }
    }

    suspend fun get(url: String): HttpResponse = _client.get(url)

    suspend fun makeExpiredRequest() {
        val mockUrl = server.url("/unauthorized-endpoint").toString()

        val response = _client.get(mockUrl)
    }
}