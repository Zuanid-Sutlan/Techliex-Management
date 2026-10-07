package com.techliexai.management.presetation.screen.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(DashboardState(
        name = "Admin User",
        userRole = "Admin",
        isAdmin = true,
        products = 5,
        activeOrders = 3,
        totalEarnings = "$150.00"
    ))
    val state = _state.asStateFlow()

    val user = mutableStateOf(User(username = "admin", name = "Admin User", role = "Admin"))

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                if (it.username.isNotBlank()) {
                    _state.value = _state.value.copy(
                        name = it.name,
                        userRole = it.role,
                        isAdmin = it.role == "Admin"
                    )
                    user.value = it
                }
            }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.OnNavigateContentClicked -> {
                when (action.item) {
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
            }
        }
    }
}
