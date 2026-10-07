package com.techliexai.management.data.repository

import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserPreferencesRepositoryFake : UserPreferencesRepository {
    private val currentUser = MutableStateFlow(User(id = 1, username = "admin", name = "Admin User", role = "Admin"))

    override suspend fun saveUser(user: User) {
        currentUser.value = user
    }

    override fun getUser(): Flow<User> = currentUser

    override suspend fun clearUser() {
        currentUser.value = User()
    }
}
