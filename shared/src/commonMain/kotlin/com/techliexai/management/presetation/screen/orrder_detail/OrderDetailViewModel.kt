package com.techliexai.management.presetation.screen.orrder_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.Result
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
            is OrderDetailScreenAction.OnEditClicked -> {
            }
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
            when (val result = orderRepository.getOrderById(orderId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(order = result.data)
                }
                is Result.Failure -> {
                    EventManager.showMessage("Order not found", MessageType.ERROR)
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
            val currentOrder = _state.value.order
            if (currentOrder != null) {
                val updatedOrder = currentOrder.copy(
                    trackId = trackingId,
                    company = company,
                    status = "Shipped"
                )
                when (val result = orderRepository.updateOrder(updatedOrder)) {
                    is Result.Success -> {
                        EventManager.hideLoading()
                        EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)
                        _state.value = _state.value.copy(order = updatedOrder)
                    }
                    is Result.Failure -> {
                        EventManager.hideLoading()
                        EventManager.showMessage("Update failed", MessageType.ERROR)
                    }
                }
            } else {
                EventManager.hideLoading()
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
            val currentOrder = _state.value.order
            if (currentOrder != null) {
                val updatedOrder = currentOrder.copy(status = "Completed")
                when (val result = orderRepository.updateOrder(updatedOrder)) {
                    is Result.Success -> {
                        EventManager.hideLoading()
                        EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)
                        _state.value = _state.value.copy(order = updatedOrder)
                    }
                    is Result.Failure -> {
                        EventManager.hideLoading()
                        EventManager.showMessage("Update failed", MessageType.ERROR)
                    }
                }
            } else {
                EventManager.hideLoading()
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
