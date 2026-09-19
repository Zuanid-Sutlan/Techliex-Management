package com.techliexai.management.presetation.screen.dashboard

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems
import com.techliexai.management.presetation.utils.Constants
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val accountRef = FirebaseDatabase.getAccountReference()
    private val productRef = FirebaseDatabase.getProductHuntReference()
    private val orderRef = FirebaseDatabase.getOrderReference()

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    val user = mutableStateOf(User())

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(
                    name = it.name,
                    userRole = it.role,
                    isAdmin = it.role == "Admin"
                )
                user.value = it
            }
        }
        viewModelScope.launch {
            try {
                productRef.snapshots.collect { snapshot ->
                    val productCount = snapshot.documents.size
                    _state.value = _state.value.copy(products = productCount)
                }
            } catch (e: Exception) {
                EventManager.showMessage(e.message ?: "Unknown error", MessageType.ERROR)
            }
        }
        viewModelScope.launch {
            try {
                orderRef.snapshots.collect { snapshot ->
                    val orderCount = snapshot.documents.map { it.data<Order>() }
                    val myOrders = orderCount.filter {
                        it.addedBy == user.value.name.ifEmpty { Constants.getUser().name } || user.value.role.ifEmpty { Constants.getUser().role } == "Admin"
                    }
                    _state.value =
                        _state.value.copy(
                            activeOrders = myOrders.filter { it.status == "Active" }.size,
                            totalEarnings = "$${myOrders.sumOf { it.listingPrice.toDouble() }}"
                        )
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }


    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnNavigateContentClicked -> {
                when (action.item) {
//                    DrawerItems.DASHBOARD -> EventManager.navigateTo(Screen.DashboardScreen)
                    DrawerItems.MEMBERS -> EventManager.navigateTo(Screen.MembersScreen)
                    DrawerItems.PRODUCTS -> EventManager.navigateTo(Screen.HuntProductScreen)
                    DrawerItems.ORDERS -> EventManager.navigateTo(Screen.OrdersScreen)
                    DrawerItems.LOGOUT -> {
                        viewModelScope.launch {
                            userPreferencesRepository.clearUser()
                        }
                        EventManager.navigateTo(Screen.LoginScreen)
                        EventManager.showMessage("Logged out successfully", MessageType.SUCCESS)
                    }

                    else -> {}
                }
//                _state.value = _state.value.copy(selectedItem = action.item)
            }
        }
    }

}