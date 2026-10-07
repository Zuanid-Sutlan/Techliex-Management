package com.techliexai.management.domain.repository

import com.techliexai.management.domain.model.User

sealed class DataError {
    object NoInternet : DataError()
    object RequestTimeout : DataError()
    object ServerError : DataError()
    object AuthenticationError : DataError()
    data class Unknown(val message: String?) : DataError()
    object Serialization : DataError()
}

sealed class Result<out T, out E> {
    data class Success<out T>(val data: T) : Result<T, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
}

interface UserRepository {
    suspend fun createUser(user: User): Result<String, DataError>
    suspend fun getUserByUsername(username: String): Result<User, DataError>
    suspend fun updateUser(user: User): Result<User, DataError>
    suspend fun deleteUser(user: User): Result<Unit, DataError>
    suspend fun getAllUsers(): Result<List<User>, DataError>
}
