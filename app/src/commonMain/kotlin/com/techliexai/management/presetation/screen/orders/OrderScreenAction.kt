package com.techliexai.management.presetation.screen.orders

import com.techliexai.management.domain.model.Order

sealed interface OrderScreenAction {

    data object OnNavigateBackClicked : OrderScreenAction
    data class OnSearchQueryChanged(val query: String) : OrderScreenAction

    data object OnAddOrderClicked : OrderScreenAction
    data class OnOrderClicked(val order: Order) : OrderScreenAction


}