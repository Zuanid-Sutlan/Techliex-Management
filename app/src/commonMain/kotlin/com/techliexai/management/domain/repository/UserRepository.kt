package com.techliexai.management.domain.repository

import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.User

interface UserRepository {

    suspend fun createUser(user: User): Result<String, DataError>

    suspend fun getUserByUsername(username: String): Result<User, DataError>

    suspend fun updateUser(user: User): Result<User, DataError>

    suspend fun deleteUser(user: User): Result<Unit, DataError>


}