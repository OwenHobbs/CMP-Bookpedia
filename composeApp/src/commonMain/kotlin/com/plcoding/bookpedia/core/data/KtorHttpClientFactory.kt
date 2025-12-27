package com.plcoding.bookpedia.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

object KtorHttpClientFactory {

    fun create(engine: HttpClientEngine): HttpClient {
        // TODO: review everything
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json( // use kotlinx.serialization
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

}