package com.techliexai.management.presetation.screen.member_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.Result
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow(CreateUserScreenState())
    val state = _state.asStateFlow()

    fun onAction(action: CreateUserAction) {
        when (action) {
            is CreateUserAction.OnNameChanged -> {
                _state.value = _state.value.copy(name = action.name)
            }

            is CreateUserAction.OnUsernameChanged -> {
                _state.value = _state.value.copy(username = action.username)
            }

            is CreateUserAction.OnPasswordChanged -> {
                _state.value = _state.value.copy(password = action.password)
            }

            is CreateUserAction.OnRoleSelected -> {
                _state.value = _state.value.copy(role = action.role)
            }

            is CreateUserAction.OnSaveUser -> createUser()

            is CreateUserAction.OnBackClicked -> {
                _state.value = _state.value.copy(name = "", username = "", password = "", role = "")
                EventManager.navigateBack()
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            EventManager.showLoading()
            try {
                if (!isInternetAvailable()) {
                    EventManager.showMessage("No internet connection", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                if (_state.value.name.isEmpty() || _state.value.username.isEmpty() || _state.value.password.isEmpty() || _state.value.role.isEmpty()){
                    EventManager.showMessage("Please fill all the fields", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                val newUser = User(
                    id = (_state.value.username.hashCode() and 0x7FFFFFFF),
                    name = _state.value.name,
                    username = _state.value.username,
                    password = _state.value.password,
                    role = _state.value.role,
                    isActive = true
                )
                when (val result = userRepository.createUser(newUser)) {
                    is Result.Success -> {
                        EventManager.showMessage("User created successfully", MessageType.SUCCESS)
                        EventManager.navigateBack()
                        _state.value = _state.value.copy(name = "", username = "", password = "", role = "")
                    }
                    is Result.Failure -> {
                        EventManager.showMessage("Failed to create user", MessageType.ERROR)
                    }
                }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
            } finally {
                EventManager.hideLoading()
            }
        }
    }
}
