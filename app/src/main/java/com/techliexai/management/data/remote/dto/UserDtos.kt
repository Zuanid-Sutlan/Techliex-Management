package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val username: String,
    val password: String,
    val name: String,
    val role: String = "Member"
)
