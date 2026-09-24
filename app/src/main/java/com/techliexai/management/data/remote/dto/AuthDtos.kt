package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class UserDto(
    val id: Long? = null,
    val username: String = "",
    val name: String = "",
    val role: String = "Member",
    val isActive: Boolean? = true,
    val createdAt: String? = null
)

@Serializable
data class LoginResponse(
    val token: String = "",
    val user: UserDto? = null
)
