package com.techliexai.management.presetation.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.repository.AuthRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.Constants
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import com.techliexai.management.presetation.utils.openWhatsAppChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val context: Context,
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.update { currentState -> currentState.copy(username = it.username) }
            }
        }
    }

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.OnUsernameChanged -> {
                _state.update { currentState -> currentState.copy(username = action.username.trim()) }
            }

            is LoginScreenAction.OnPasswordChanged -> {
                _state.update { currentState -> currentState.copy(password = action.password.trim()) }
            }

            is LoginScreenAction.OnPasswordVisibilityClicked -> {
                _state.update { currentState -> currentState.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
            }

            is LoginScreenAction.OnContactAdminClicked -> {
                openWhatsAppChat(context, Constants.AdminNumber)
            }

            is LoginScreenAction.OnDeveloperInfoClicked -> {
                openWhatsAppChat(context, Constants.DeveloperNumber)
            }

            is LoginScreenAction.OnLoginClicked -> validateUserAndLogin()
        }
    }

    private fun validateUserAndLogin() {
        viewModelScope.launch {
            EventManager.showLoading()
            if (!isInternetAvailable(context)) {
                EventManager.showMessage("No internet connection", MessageType.ERROR)
                EventManager.hideLoading()
                return@launch
            }
            val result = authRepository.login(_state.value.username, _state.value.password)
            EventManager.hideLoading()
            when (result) {
                is Result.Success -> {
                    Constants.setUser(result.data)
                    EventManager.navigateTo(Screen.DashboardScreen)
                    EventManager.showMessage("Login successful", MessageType.SUCCESS)
                }

                is Result.Failure -> {
                    val errorMsg = when (val error = result.error) {
                        is DataError.AuthenticationError -> "Invalid username or password"
                        is DataError.NoInternet -> "No internet connection"
                        is DataError.RequestTimeout -> "Request timed out"
                        is DataError.ServerError -> "Server error"
                        is DataError.Unknown -> error.message ?: "Login failed"
                        else -> "Login failed"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }
}
