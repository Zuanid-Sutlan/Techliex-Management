package com.techliexai.management.presetation.screen.login

sealed interface LoginScreenAction {
    data class OnUsernameChanged(val username: String) : LoginScreenAction
    data class OnPasswordChanged(val password: String) : LoginScreenAction
    object OnLoginClicked : LoginScreenAction
    object OnPasswordVisibilityClicked : LoginScreenAction
    object OnContactAdminClicked : LoginScreenAction
    object OnDeveloperInfoClicked: LoginScreenAction
}