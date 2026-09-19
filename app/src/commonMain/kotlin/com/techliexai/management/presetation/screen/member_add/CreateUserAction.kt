package com.techliexai.management.presetation.screen.member_add

sealed interface CreateUserAction {
    data class OnNameChanged(val name: String) : CreateUserAction
    data class OnUsernameChanged(val username: String) : CreateUserAction
    data class OnPasswordChanged(val password: String) : CreateUserAction
    data class OnRoleSelected(val role: String) : CreateUserAction
    object OnSaveUser : CreateUserAction
    object OnBackClicked : CreateUserAction
}