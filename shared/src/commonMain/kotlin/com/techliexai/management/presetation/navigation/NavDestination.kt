package com.techliexai.management.presetation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavDestination(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard, Screen.DashboardScreen),
    MEMBERS("Members", Icons.Default.Group, Screen.MembersScreen),
    PRODUCTS("Products", Icons.Default.Inventory, Screen.HuntProductScreen),
    ORDERS("Orders", Icons.Default.AddShoppingCart, Screen.OrdersScreen),
    SETTINGS("Settings", Icons.Default.Settings, Screen.SettingsScreen)
}
