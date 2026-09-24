package com.techliexai.management.data.remote

import com.techliexai.management.data.utils.Constant
import com.techliexai.management.domain.repository.UserPreferencesRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClientFactory {

    fun create(userPreferencesRepository: UserPreferencesRepository): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(Constant.BASE_URL)
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = userPreferencesRepository.getTokenSync()
                        if (!token.isNullOrBlank()) {
                            BearerTokens(token, "")
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        request.url.host == "10.0.2.2" || request.url.host == "192.168.100.212"
                    }
                }
            }
        }
    }
}
