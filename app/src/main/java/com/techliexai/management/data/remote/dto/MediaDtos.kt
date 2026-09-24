package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val imageUrl: String = ""
)
