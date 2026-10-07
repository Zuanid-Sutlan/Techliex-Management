package com.techliexai.management.presetation.screen.members

import androidx.lifecycle.ViewModel
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserRepository
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberScreenViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow(MemberState(
        members = listOf(
            User(id = 1, username = "admin", name = "Admin User", role = "Admin", isActive = true),
            User(id = 2, username = "manager", name = "Manager User", role = "Manager", isActive = true)
        )
    ))
    val state = _state.asStateFlow()

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
}
