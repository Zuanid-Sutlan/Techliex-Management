package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.LoginRequest
import com.techliexai.management.data.remote.dto.LoginResponse
import com.techliexai.management.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface AuthApiService {
    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse>
    suspend fun getMe(): ApiResponse<UserDto>
}

class AuthApiServiceImpl(private val client: HttpClient) : AuthApiService {
    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return client.post("/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getMe(): ApiResponse<UserDto> {
        return client.get("/api/v1/auth/me").body()
    }
}
