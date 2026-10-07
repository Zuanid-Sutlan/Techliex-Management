package com.techliexai.management.presetation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect { user ->
                if (user.username.isNotBlank()) {
                    _state.update { it.copy(username = user.username) }
                }
            }
        }
    }

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.OnUsernameChanged -> {
                _state.update { it.copy(username = action.username.trim()) }
            }
            is LoginScreenAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password.trim()) }
            }
            is LoginScreenAction.OnPasswordVisibilityClicked -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginScreenAction.OnContactAdminClicked -> {
                EventManager.showMessage("Contact Admin at +1234567890", MessageType.INFO)
            }
            is LoginScreenAction.OnDeveloperInfoClicked -> {
                EventManager.showMessage("Developer: Zunaid Sultan", MessageType.INFO)
            }
            is LoginScreenAction.OnLoginClicked -> {
                validateUserAndLogin()
            }
        }
    }

    private fun validateUserAndLogin() {
        viewModelScope.launch {
            EventManager.showLoading()
            val username = _state.value.username
            val password = _state.value.password

            if (username == "admin" && password == "admin") {
                val user = User(id = 1, username = "admin", name = "Admin User", role = "Admin")
                userPreferencesRepository.saveUser(user)
                EventManager.navigateTo(Screen.DashboardScreen)
                EventManager.showMessage("Login successful", MessageType.SUCCESS)
            } else {
                EventManager.showMessage("Invalid username or password", MessageType.ERROR)
            }
            EventManager.hideLoading()
        }
    }
}
