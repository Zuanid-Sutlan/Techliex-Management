package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.UserApiService
import com.techliexai.management.data.remote.dto.CreateUserRequest
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun createUser(user: User): Result<String, DataError> {
        return safeCall {
            val request = CreateUserRequest(
                username = user.username,
                password = user.password,
                name = user.name,
                role = user.role.ifEmpty { "Member" }
            )
            val response = userApiService.createUser(request)
            if (response.success) {
                response.message ?: "User created successfully"
            } else {
                throw Exception(response.message ?: "Failed to create user")
            }
        }
    }

    override suspend fun getUsers(): Result<List<User>, DataError> {
        return safeCall {
            val response = userApiService.getUsers()
            if (response.success && response.data != null) {
                response.data.map { it.toDomainUser() }
            } else {
                throw Exception(response.message ?: "Failed to fetch users")
            }
        }
    }

    override suspend fun getUserByUsername(username: String): Result<User, DataError> {
        return safeCall {
            val response = userApiService.getUserByUsername(username)
            if (response.success && response.data != null) {
                response.data.toDomainUser()
            } else {
                throw Exception(response.message ?: "User not found")
            }
        }
    }

    override suspend fun deleteUser(username: String): Result<Unit, DataError> {
        return safeCall {
            val response = userApiService.deleteUser(username)
            if (response.success) {
                Unit
            } else {
                throw Exception(response.message ?: "Failed to delete user")
            }
        }
    }
}
