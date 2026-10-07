package com.techliexai.management.data.repository

import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.DataError
import com.techliexai.management.domain.repository.Result
import com.techliexai.management.domain.repository.UserRepository

class UserRepositoryFake : UserRepository {
    private val users = mutableMapOf(
        "admin" to User(id = 1, username = "admin", password = "admin", name = "Admin User", role = "Admin", isActive = true),
        "manager" to User(id = 2, username = "manager", password = "password", name = "Manager User", role = "Manager", isActive = true)
    )

    override suspend fun createUser(user: User): Result<String, DataError> {
        users[user.username] = user
        return Result.Success("User created successfully")
    }

    override suspend fun getUserByUsername(username: String): Result<User, DataError> {
        val user = users[username]
        return if (user != null) {
            Result.Success(user)
        } else {
            Result.Failure(DataError.ServerError)
        }
    }

    override suspend fun updateUser(user: User): Result<User, DataError> {
        users[user.username] = user
        return Result.Success(user)
    }

    override suspend fun deleteUser(user: User): Result<Unit, DataError> {
        users.remove(user.username)
        return Result.Success(Unit)
    }

    override suspend fun getAllUsers(): Result<List<User>, DataError> {
        return Result.Success(users.values.toList())
    }
}
