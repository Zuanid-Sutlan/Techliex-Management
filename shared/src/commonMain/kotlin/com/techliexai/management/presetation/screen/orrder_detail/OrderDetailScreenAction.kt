package com.techliexai.management.presetation.screen.orrder_detail

sealed interface OrderDetailScreenAction {

    data class OnLoadOrder(val orderId: Int) : OrderDetailScreenAction

    object OnNavigateBackClicked : OrderDetailScreenAction
    object OnEditClicked : OrderDetailScreenAction

    data class OnUpdateOrderDetailClicked(val trackId: String, val company: String): OrderDetailScreenAction
    object OnCompleteClicked : OrderDetailScreenAction


}