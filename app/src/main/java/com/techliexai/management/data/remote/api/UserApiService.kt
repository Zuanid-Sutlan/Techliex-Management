package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.CreateUserRequest
import com.techliexai.management.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface UserApiService {
    suspend fun createUser(request: CreateUserRequest): ApiResponse<UserDto>
    suspend fun getUsers(): ApiResponse<List<UserDto>>
    suspend fun getUserByUsername(username: String): ApiResponse<UserDto>
    suspend fun deleteUser(username: String): ApiResponse<Unit>
}

class UserApiServiceImpl(private val client: HttpClient) : UserApiService {
    override suspend fun createUser(request: CreateUserRequest): ApiResponse<UserDto> {
        return client.post("/api/v1/users") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getUsers(): ApiResponse<List<UserDto>> {
        return client.get("/api/v1/users").body()
    }

    override suspend fun getUserByUsername(username: String): ApiResponse<UserDto> {
        return client.get("/api/v1/users/$username").body()
    }

    override suspend fun deleteUser(username: String): ApiResponse<Unit> {
        return client.delete("/api/v1/users/$username").body()
    }
}
