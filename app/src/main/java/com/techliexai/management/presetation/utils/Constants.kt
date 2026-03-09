package com.techliexai.management.presetation.utils

import androidx.compose.runtime.mutableStateOf
import com.techliexai.management.domain.model.User

object Constants {

    private val user = mutableStateOf(User())

    fun setUser(user: User) {
        this.user.value = user
    }
    fun getUser(): User {
        return user.value
    }

    const val defaultUsername = ""
    const val defaultPassword = ""



    const val AdminNumber = "923336575847"
    const val DeveloperNumber = "923214293781"
}