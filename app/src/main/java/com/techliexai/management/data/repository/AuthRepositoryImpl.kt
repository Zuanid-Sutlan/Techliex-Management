package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.AuthApiService
import com.techliexai.management.data.remote.dto.LoginRequest
import com.techliexai.management.data.remote.dto.UserDto
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.AuthRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User, DataError> {
        return safeCall {
            val response = authApiService.login(LoginRequest(username = username, password = password))
            if (response.success && response.data != null) {
                val loginData = response.data
                userPreferencesRepository.saveToken(loginData.token)
                val user = loginData.user?.toDomainUser() ?: User(username = username)
                userPreferencesRepository.saveUser(user)
                user
            } else {
                throw Exception(response.message ?: "Login failed")
            }
        }
    }

    override suspend fun getMe(): Result<User, DataError> {
        return safeCall {
            val response = authApiService.getMe()
            if (response.success && response.data != null) {
                val user = response.data.toDomainUser()
                userPreferencesRepository.saveUser(user)
                user
            } else {
                throw Exception(response.message ?: "Failed to get profile")
            }
        }
    }
}

fun UserDto.toDomainUser(): User {
    return User(
        id = id?.toInt() ?: -1,
        username = username,
        password = "",
        name = name,
        role = role,
        isActive = isActive ?: true
    )
}
