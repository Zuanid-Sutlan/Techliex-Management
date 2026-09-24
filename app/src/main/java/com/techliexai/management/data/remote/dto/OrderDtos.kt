package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val orderDate: String,
    val productId: Long,
    val address: String,
    val variationNote: String? = null,
    val quantity: Int,
    val listingPrice: Double,
    val paymentImageUrl: String
)

@Serializable
data class UpdateLogisticsRequest(
    val trackId: String,
    val company: String
)

@Serializable
data class OrderDto(
    val id: Long = 0,
    val orderDate: String? = null,
    val userId: Long? = null,
    val userName: String? = null,
    val productId: Long? = null,
    val productTitle: String? = null,
    val address: String? = null,
    val variationNote: String? = null,
    val quantity: Int? = null,
    val listingPrice: Double? = null,
    val paymentImageUrl: String? = null,
    val trackId: String? = null,
    val company: String? = null,
    val status: String? = null,
    val createdAt: String? = null
)
