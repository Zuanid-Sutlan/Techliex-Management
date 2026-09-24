package com.techliexai.management.presetation.screen.orders

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.Constants
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val orderRepository: OrderRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderScreenState())
    val state = _state.asStateFlow()

    val user = mutableStateOf(User())

    init {
        checkIsAdmin()
        fetchOrders()
    }

    fun onAction(action: OrderScreenAction) {
        when (action) {
            is OrderScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is OrderScreenAction.OnSearchQueryChanged -> {
                _state.value = _state.value.copy(searchQuery = action.query)
            }

            is OrderScreenAction.OnAddOrderClicked -> {
                EventManager.navigateTo(Screen.AddOrderScreen)
            }

            is OrderScreenAction.OnOrderClicked -> {
                EventManager.navigateTo(Screen.OrderDetailScreen(action.order.id))
            }
        }
    }

    fun fetchOrders() {
        viewModelScope.launch {
            when (val result = orderRepository.getOrders()) {
                is Result.Success -> {
                    val orders = result.data
                    val filteredOrders = orders.filter {
                        it.addedBy == user.value.name.ifEmpty { Constants.getUser().name } ||
                                user.value.role.ifEmpty { Constants.getUser().role } == "Admin"
                    }
                    val sortedOrders = filteredOrders.sortedBy {
                        when (it.status) {
                            "Active" -> 0
                            "Shipped" -> 1
                            "Completed" -> 2
                            else -> 3
                        }
                    }
                    _state.value = _state.value.copy(orders = sortedOrders)
                }

                is Result.Failure -> {
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Failed to fetch orders"
                        else -> "Failed to fetch orders"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }

    fun checkIsAdmin() {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(currentUserId = it.id)
                user.value = it
            }
        }
    }
}
