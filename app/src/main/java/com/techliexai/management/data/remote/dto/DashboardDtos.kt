package com.techliexai.management.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardStatsDto(
    val productCount: Long = 0,
    val activeOrdersCount: Long = 0,
    val totalEarnings: Double = 0.0
)
