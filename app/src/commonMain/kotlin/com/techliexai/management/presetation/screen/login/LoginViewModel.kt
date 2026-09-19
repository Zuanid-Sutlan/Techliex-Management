package com.techliexai.management.presetation.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.Constants
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import com.techliexai.management.presetation.utils.openWhatsAppChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val accountRef = FirebaseDatabase.getAccountReference()

    private val _state =
        MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.update { _state.value.copy(username = it.username) }
            }
        }
    }

    fun onAction(action: LoginScreenAction) {
        when (action) {
            is LoginScreenAction.OnUsernameChanged -> {
                _state.update { _state.value.copy(username = action.username.trim()) }
            }

            is LoginScreenAction.OnPasswordChanged -> {
                _state.update { _state.value.copy(password = action.password.trim()) }
            }

            is LoginScreenAction.OnPasswordVisibilityClicked -> {
                _state.update { _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible) }
            }

            is LoginScreenAction.OnContactAdminClicked -> {
                openWhatsAppChat(Constants.AdminNumber)
            }

            is LoginScreenAction.OnDeveloperInfoClicked -> {
                openWhatsAppChat(Constants.DeveloperNumber)
            }

            is LoginScreenAction.OnLoginClicked -> validateUserAndLogin()
        }
    }

    private fun validateUserAndLogin() {
        viewModelScope.launch {
            EventManager.showLoading()
            try {
                if (!isInternetAvailable()) {
                    EventManager.showMessage("No internet connection", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                val snapshot = accountRef.document(_state.value.username).get()
                if (snapshot.exists) {
                    val user = snapshot.data<User>()
                    if (user.password == _state.value.password) {
                        EventManager.navigateTo(Screen.DashboardScreen)
                        viewModelScope.launch {
                            userPreferencesRepository.saveUser(user)
                            Constants.setUser(user)
                        }
                        EventManager.showMessage("Login successful", MessageType.SUCCESS)
                        EventManager.hideLoading()
                    } else {
                        EventManager.showMessage("Incorrect password", MessageType.ERROR)
                        EventManager.hideLoading()
                    }
                } else {
                    EventManager.showMessage("User not found", MessageType.ERROR)
                    EventManager.hideLoading()
                }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
                EventManager.hideLoading()
            }

        }
    }

}