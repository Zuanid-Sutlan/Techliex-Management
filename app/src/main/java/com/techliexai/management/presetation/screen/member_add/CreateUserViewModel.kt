package com.techliexai.management.presetation.screen.member_add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.User
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CreateUserViewModel(private val context: Context) : ViewModel() {

    private val accountRef = Firebase.getAccountReference()

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
                // Handle back navigation logic here
                _state.value = _state.value.copy(name = "", username = "", password = "", role = "")
                EventManager.navigateBack()
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            EventManager.showLoading()
            try {
                if (!isInternetAvailable(context)) {
                    EventManager.showMessage("No internet connection", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                if (_state.value.name.isEmpty() || _state.value.username.isEmpty() || _state.value.password.isEmpty() || _state.value.role.isEmpty()){
                    EventManager.showMessage("Please fill all the fields", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                val id = accountRef.get().await().childrenCount.toInt() + 1
                accountRef.child(_state.value.username).setValue(User(id = id, name = _state.value.name, username = _state.value.username, password = _state.value.password, role = _state.value.role, isActive = true))
                    .addOnSuccessListener {
                        EventManager.showMessage("User created successfully", MessageType.SUCCESS)
                        EventManager.navigateBack()
                        EventManager.hideLoading()
                        _state.value = _state.value.copy(name = "", username = "", password = "", role = "")
                    }.addOnFailureListener {
                        EventManager.showMessage(it.message.toString(), MessageType.ERROR)
                        EventManager.hideLoading()
                    }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
                EventManager.hideLoading()
            }
        }
    }

}