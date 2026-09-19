package com.techliexai.management.presetation.screen.orrder_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val orderRef = FirebaseDatabase.getOrderReference()

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
            try {
                val snapshot = orderRef.document("Order_$orderId").get()
                if(snapshot.exists) {
                    val order = snapshot.data<Order>()
                    _state.value = _state.value.copy(order = order)
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    private fun updateOrderLogistics(orderId: Int, trackingId: String, company: String) {
        // 1. Guard clause for invalid IDs
        if (orderId <= 0) {
            EventManager.showMessage("Invalid Order ID", MessageType.ERROR)
            return
        }

        viewModelScope.launch {
            EventManager.showLoading()

            try {
                orderRef.document("Order_$orderId").update(
                    "trackId" to trackingId,
                    "company" to company
                )
                EventManager.hideLoading()
                EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)

                val currentOrder =
                    _state.value.order?.copy(trackId = trackingId, company = company)
                _state.value = _state.value.copy(order = currentOrder)
            } catch (e: Exception) {
                EventManager.hideLoading()
                EventManager.showMessage("Update failed: ${e.message}", MessageType.ERROR)
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

            try {
                orderRef.document("Order_$orderId").update("status" to "Completed")
                EventManager.hideLoading()
                EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)

                val currentOrder =
                    _state.value.order?.copy(status = "Completed")
                _state.value = _state.value.copy(order = currentOrder)
            } catch (e: Exception) {
                EventManager.hideLoading()
                EventManager.showMessage("Update failed: ${e.message}", MessageType.ERROR)
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