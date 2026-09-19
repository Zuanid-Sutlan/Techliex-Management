package com.techliexai.management.presetation.screen.login

import com.techliexai.management.presetation.utils.Constants

data class LoginScreenState(
    val username: String = Constants.defaultUsername,
    val password: String = Constants.defaultPassword,
    val isPasswordVisible: Boolean = false,
)
