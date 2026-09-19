package com.techliexai.management.domain.repository

import com.techliexai.management.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun saveUser(user: User)
    fun getUser(): Flow<User>

    suspend fun clearUser()

}