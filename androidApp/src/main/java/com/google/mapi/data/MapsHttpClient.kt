package com.google.mapi.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MapsHttpClient() {
    fun getClient() = HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }

        install(Logging) { logger = Logger.SIMPLE }

        defaultRequest {
            url {
                host = "maps.googleapis.com/maps/api"
                protocol = URLProtocol.HTTPS
                parameters.append("key", GOOGLE_API_KEY)
            }
        }
    }
}