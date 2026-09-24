package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequest(
    val title: String,
    val description: String? = null,
    val productImageUrl: String,
    val sourceLink: String,
    val sourcePrice: Double,
    val referenceLink: String,
    val referencePrice: Double,
    val shareWithUsernames: List<String> = emptyList()
)

@Serializable
data class UpdateWarehouseRequest(
    val warehousePrice: Double,
    val warehouseNote: String? = null
)

@Serializable
data class ProductDto(
    val id: Long = 0,
    val userId: Long? = null,
    val creatorName: String? = null,
    val title: String = "",
    val description: String? = null,
    val productImageUrl: String = "",
    val sourceLink: String = "",
    val sourcePrice: Double = 0.0,
    val referenceLink: String = "",
    val referencePrice: Double = 0.0,
    val warehousePrice: Double? = null,
    val warehouseNote: String? = null,
    val sharedUsernames: List<String> = emptyList(),
    val createdAt: String? = null
)
