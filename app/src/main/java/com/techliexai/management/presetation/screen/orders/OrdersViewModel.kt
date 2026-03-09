package com.techliexai.management.presetation.screen.orders

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.Constants
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrdersViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    private val orderRef = Firebase.getOrderReference()

    private val _state = MutableStateFlow(OrderScreenState())
    val state = _state.asStateFlow()

    val user = mutableStateOf(User())

    init {
        checkIsAdmin()
        fetchOrders()
    }

    fun onAction(action: OrderScreenAction){
        when(action){
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

    fun fetchOrders(){
        viewModelScope.launch {
            orderRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                    val filteredOrders = orders.filter {
                        it.addedBy == user.value.name.ifEmpty { Constants.getUser().name } || user.value.role.ifEmpty { Constants.getUser().role } == "Admin"
                    }
                    val sortedOrders = filteredOrders.sortedBy {
                        when(it.status){
                            "Active" -> 0
                            "Shipped" -> 1
                            "Completed" -> 2
                            else -> 3
                        }
                    }
                    _state.value = _state.value.copy(orders = sortedOrders)
                }

                override fun onCancelled(error: DatabaseError) {
                    EventManager.showMessage(error.message, MessageType.ERROR)
                }

            })
        }
    }

    fun checkIsAdmin(){
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(currentUserId = it.id)
                user.value = it
            }
        }
    }

}