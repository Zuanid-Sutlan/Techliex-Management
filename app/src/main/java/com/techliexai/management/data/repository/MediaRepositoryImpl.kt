package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.MediaApiService
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val mediaApiService: MediaApiService
) : MediaRepository {

    override suspend fun uploadImage(bytes: ByteArray, fileName: String): Result<String, DataError> {
        return safeCall {
            val response = mediaApiService.uploadImage(bytes, fileName)
            if (response.success && response.data != null) {
                response.data.imageUrl
            } else {
                throw Exception(response.message ?: "Failed to upload media")
            }
        }
    }
}
