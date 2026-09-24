package com.techliexai.management.domain.repository

import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result

interface MediaRepository {
    suspend fun uploadImage(bytes: ByteArray, fileName: String): Result<String, DataError>
}
