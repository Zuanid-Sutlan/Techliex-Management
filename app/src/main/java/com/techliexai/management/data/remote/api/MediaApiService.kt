package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.UploadResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

interface MediaApiService {
    suspend fun uploadImage(bytes: ByteArray, fileName: String): ApiResponse<UploadResponse>
}

class MediaApiServiceImpl(private val client: HttpClient) : MediaApiService {
    override suspend fun uploadImage(bytes: ByteArray, fileName: String): ApiResponse<UploadResponse> {
        return client.submitFormWithBinaryData(
            url = "/api/v1/media/upload",
            formData = formData {
                append("file", bytes, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                })
            }
        ).body()
    }
}
