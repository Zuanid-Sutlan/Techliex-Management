package com.techliexai.management.presetation.screen.login

sealed class LoginScreenAction {
    data class OnUsernameChanged(val username: String) : LoginScreenAction()
    data class OnPasswordChanged(val password: String) : LoginScreenAction()
    object OnPasswordVisibilityClicked : LoginScreenAction()
    object OnContactAdminClicked : LoginScreenAction()
    object OnDeveloperInfoClicked : LoginScreenAction()
    object OnLoginClicked : LoginScreenAction()
}
