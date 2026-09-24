package com.techliexai.management.presetation.screen.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.DashboardRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

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
                loadDashboardStats()
            }
        }
    }

    fun loadDashboardStats() {
        viewModelScope.launch {
            when (val result = dashboardRepository.getDashboardStats()) {
                is Result.Success -> {
                    val stats = result.data
                    _state.value = _state.value.copy(
                        products = stats.productCount.toInt(),
                        activeOrders = stats.activeOrdersCount.toInt(),
                        totalEarnings = "$${stats.totalEarnings}"
                    )
                }

                is Result.Failure -> {
                    // Handle failure gracefully
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
