package com.techliexai.management.presetation.screen.orders

import com.techliexai.management.domain.model.Order

data class OrderScreenState(
    val orders: List<Order> = emptyList(),
    val searchQuery: String = "",
    val currentUserId: Int = -1
)

