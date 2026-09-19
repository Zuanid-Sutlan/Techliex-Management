package com.techliexai.management.presetation.screen.dashboard

import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems
import com.techliexai.management.presetation.screen.members.MemberState
import kotlinx.serialization.Serializable

@Serializable
data class DashboardState(
    val name: String = "Zunaid Sultan",
    val userRole: String = "Student", // or "Mentor"
    val isAdmin: Boolean = false,
    val selectedItem: DrawerItems = DrawerItems.DASHBOARD,
    val products: Int = 5,
    val activeOrders: Int = 2,
    val totalEarnings: String = "$1,240.00",


    val membersState: MemberState = MemberState()
)
