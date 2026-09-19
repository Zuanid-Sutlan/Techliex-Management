package com.techliexai.management.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = -1,
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val role: String = "",
    val isActive: Boolean = false
)
