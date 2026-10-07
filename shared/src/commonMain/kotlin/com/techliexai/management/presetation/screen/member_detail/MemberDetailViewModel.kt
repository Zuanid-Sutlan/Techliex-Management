package com.techliexai.management.presetation.screen.member_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.repository.Result
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow(MemberDetailScreeState())
    val state = _state.asStateFlow()

    fun onAction(action: MemberDetailAction) {
        when (action) {
            is MemberDetailAction.OnLoadMember -> {
                loadMemberDetails(action.username)
            }
            MemberDetailAction.OnBackClicked -> EventManager.navigateBack()
            MemberDetailAction.OnDeleteMemberClicked -> deleteMember()
            MemberDetailAction.OnEditMemberClicked -> {
            }
        }
    }

    fun loadMemberDetails(username: String) {
        viewModelScope.launch {
            try {
                when (val result = userRepository.getUserByUsername(username)) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(user = result.data)
                    }
                    is Result.Failure -> {
                        EventManager.showMessage("User not found", MessageType.ERROR)
                    }
                }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
            }
        }
    }

    fun deleteMember(){
        viewModelScope.launch {
            EventManager.showLoading()
            try {
                when (val result = userRepository.deleteUser(_state.value.user)) {
                    is Result.Success -> {
                        EventManager.showMessage("Member deleted successfully", MessageType.SUCCESS)
                        EventManager.navigateBack()
                    }
                    is Result.Failure -> {
                        EventManager.showMessage("Failed to delete member", MessageType.ERROR)
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
