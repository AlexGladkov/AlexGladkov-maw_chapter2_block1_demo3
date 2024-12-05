package tech.mobiledeveloper.block1example2.network

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun setupMockWebServer(): MockWebServer {
    val mockWebServer = MockWebServer()

    mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(401)
            .setBody("Unauthorized Access")
    )

    GlobalScope.launch {
        mockWebServer.start()
    }
    return mockWebServer
}