package com.techliexai.management.presetation.screen.orrder_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val orderRef = Firebase.getOrderReference()

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
        orderRef.child("Order_$orderId").get().addOnSuccessListener {
            val order = it.getValue(Order::class.java)
            _state.value = _state.value.copy(order = order)
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

            // 2. Prepare the map of specific fields to update
            val updates = mapOf(
                "trackId" to trackingId,
                "company" to company,
                "status" to "Shipped"
            )

            // 3. Update only these specific keys in the "Order_X" node
            orderRef.child("Order_$orderId").updateChildren(updates)
                .addOnSuccessListener {
                    EventManager.hideLoading()
                    EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)

                    // 4. Update local state so the UI refreshes immediately
//                    val currentOrders = _state.value.orders.map { order ->
//                        if (order.id == orderId) {
//                            order.copy(trackId = trackingId, company = company)
//                        } else order
//                    }
                    val currentOrder =
                        _state.value.order?.copy(trackId = trackingId, company = company)
                    _state.value = _state.value.copy(order = currentOrder)
                }
                .addOnFailureListener { error ->
                    EventManager.hideLoading()
                    EventManager.showMessage("Update failed: ${error.message}", MessageType.ERROR)
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

            // 2. Prepare the map of specific fields to update
            val updates = mapOf(
                "status" to "Completed"
            )

            // 3. Update only these specific keys in the "Order_X" node
            orderRef.child("Order_$orderId").updateChildren(updates)
                .addOnSuccessListener {
                    EventManager.hideLoading()
                    EventManager.showMessage("Logistics updated successfully!", MessageType.SUCCESS)

                    // 4. Update local state so the UI refreshes immediately
//                    val currentOrders = _state.value.orders.map { order ->
//                        if (order.id == orderId) {
//                            order.copy(trackId = trackingId, company = company)
//                        } else order
//                    }
                    val currentOrder =
                        _state.value.order?.copy(status = "Completed")
                    _state.value = _state.value.copy(order = currentOrder)
                }
                .addOnFailureListener { error ->
                    EventManager.hideLoading()
                    EventManager.showMessage("Update failed: ${error.message}", MessageType.ERROR)
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