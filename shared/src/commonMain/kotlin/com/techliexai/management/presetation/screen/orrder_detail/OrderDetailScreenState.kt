package com.techliexai.management.presetation.screen.orrder_detail

import com.techliexai.management.domain.model.Order

data class OrderDetailScreenState(
    val order: Order? = null,
    val currentUserId: Int = -1,
    val isAdmin: Boolean = false,


)
