package com.techliexai.management.presetation.screen.dashboard

import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems

data class DashboardState(
    val selectedItem: DrawerItems = DrawerItems.DASHBOARD,
    val name: String = "",
    val userRole: String = "",
    val isAdmin: Boolean = false,
    val products: Int = 0,
    val activeOrders: Int = 0,
    val totalEarnings: String = "$0"
)
