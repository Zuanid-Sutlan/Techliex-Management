package com.techliexai.management.presetation.screen.orrder_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderRepository: OrderRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderDetailScreenState())
    val state = _state.asStateFlow()

    init {
        checkIsAdmin()
    }

    fun onAction(action: OrderDetailScreenAction) {
        when (action) {
            is OrderDetailScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is OrderDetailScreenAction.OnEditClicked -> {}

            is OrderDetailScreenAction.OnLoadOrder -> {
                fetchOrder(action.orderId)
            }

            is OrderDetailScreenAction.OnCompleteClicked -> {
                completeOrder(_state.value.order?.id ?: -1)
            }

            is OrderDetailScreenAction.OnUpdateOrderDetailClicked -> {
                updateOrderLogistics(_state.value.order?.id ?: -1, action.trackId, action.company)
            }
        }
    }

    private fun fetchOrder(orderId: Int) {
        viewModelScope.launch {
            when (val result = orderRepository.getOrderById(orderId.toLong())) {
                is Result.Success -> {
                    _state.value = _state.value.copy(order = result.data)
                }

                is Result.Failure -> {
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Failed to fetch order"
                        else -> "Failed to fetch order"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }

    private fun updateOrderLogistics(orderId: Int, trackingId: String, company: String) {
        if (orderId <= 0) {
            EventManager.showMessage("Invalid Order ID", MessageType.ERROR)
            return
        }

        viewModelScope.launch {
            EventManager.showLoading()
            when (val result = orderRepository.updateLogistics(orderId.toLong(), trackingId, company)) {
                is Result.Success -> {
                    EventManager.hideLoading()
                    EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)
                    _state.value = _state.value.copy(order = result.data)
                }

                is Result.Failure -> {
                    EventManager.hideLoading()
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Update failed"
                        else -> "Update failed"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }

    private fun completeOrder(orderId: Int) {
        if (orderId <= 0) {
            EventManager.showMessage("Invalid Order ID", MessageType.ERROR)
            return
        }

        viewModelScope.launch {
            EventManager.showLoading()
            when (val result = orderRepository.completeOrder(orderId.toLong())) {
                is Result.Success -> {
                    EventManager.hideLoading()
                    EventManager.showMessage("Order completed successfully!", MessageType.SUCCESS)
                    _state.value = _state.value.copy(order = result.data)
                }

                is Result.Failure -> {
                    EventManager.hideLoading()
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Update failed"
                        else -> "Update failed"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }

    fun checkIsAdmin() {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(isAdmin = it.role == "Admin")
            }
        }
    }
}
