package infrastructure.client

import infrastructure.TestEnvironment
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Отправка запросов по http/rest
 */
class RestClient(testEnvironment: TestEnvironment) : Client {
    private val urlBuilder by lazy { testEnvironment.inputUrl }
    private val client = HttpClient(OkHttp)
    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            path("$version/$path")
        }.build()

        val resp = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            accept(ContentType.Application.Json)
            setBody(request)

        }.call

        return resp.body()
    }
}
