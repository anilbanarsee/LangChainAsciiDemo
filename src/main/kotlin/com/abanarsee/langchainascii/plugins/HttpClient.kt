package com.abanarsee.shared.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

fun httpClient(): HttpClient {
    return HttpClient(CIO) {
        contentNegotiation()
        withCache()
    }
}

fun basicHttpClient(username: String, password: String): HttpClient {
    return HttpClient(CIO) {
        contentNegotiation()
        withCache()

        withBasicCredentials(username, password)
    }
}

fun httpClientWithAuthHeader(authHeader: String): HttpClient {
    return HttpClient(CIO) {
        contentNegotiation()
        withCache()
    }
}

private fun HttpClientConfig<CIOEngineConfig>.withBasicCredentials(username: String, password: String) {
    install(Auth) {
        basic {
            sendWithoutRequest { true }
            credentials {
                BasicAuthCredentials(
                    username = username,
                    password = password,
                )
            }
        }
    }
}

private fun HttpClientConfig<CIOEngineConfig>.withCache() {
    install(HttpCache) {
        val cacheFile = Files.createDirectories(Paths.get("build/cache")).toFile()
        publicStorage(FileStorage(cacheFile))
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun HttpClientConfig<CIOEngineConfig>.contentNegotiation() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}