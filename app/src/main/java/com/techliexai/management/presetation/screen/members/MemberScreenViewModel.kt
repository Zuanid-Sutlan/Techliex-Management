package com.techliexai.management.presetation.screen.members

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberScreenViewModel(
    private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MemberState())
    val state = _state.asStateFlow()

    init {
        fetchAllUsers()
    }

    fun onAction(action: MembersAction) {
        when (action) {
            MembersAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is MembersAction.OnMemberClicked -> {
                EventManager.navigateTo(screen = Screen.MemberDetailScreen(action.member.username))
            }

            MembersAction.OnAddMemberClicked -> {
                EventManager.navigateTo(screen = Screen.CreateMemberScreen)
            }
        }
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            if (!isInternetAvailable(context)) {
                EventManager.showMessage("No internet connection", MessageType.ERROR)
                EventManager.hideLoading()
                return@launch
            }
            when (val result = userRepository.getUsers()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(members = result.data)
                }

                is Result.Failure -> {
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Failed to fetch users"
                        else -> "Failed to fetch users"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }
}
