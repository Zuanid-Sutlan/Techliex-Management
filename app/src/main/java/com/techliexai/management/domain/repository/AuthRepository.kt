package com.techliexai.management.domain.repository

import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User, DataError>
    suspend fun getMe(): Result<User, DataError>
}
