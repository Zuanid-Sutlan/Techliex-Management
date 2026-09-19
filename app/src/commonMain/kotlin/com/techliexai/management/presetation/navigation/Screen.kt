package com.techliexai.management.presetation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    object LoginScreen : Screen()

    @Serializable
    object DashboardScreen : Screen()

    @Serializable
    object CreateMemberScreen : Screen()

    @Serializable
    object MembersScreen : Screen()

    @Serializable
    data class MemberDetailScreen(val username: String) : Screen()

    @Serializable
    object HuntProductScreen : Screen()

    @Serializable
    object AddProductScreen : Screen()

    @Serializable
    data class ProductDetailScreen(val productId: Int) : Screen()

    @Serializable
    data object OrdersScreen: Screen()

    @Serializable
    data object AddOrderScreen : Screen()

    @Serializable
    data class OrderDetailScreen(val orderId: Int) : Screen()




    @Serializable
    object SettingsScreen : Screen()

}