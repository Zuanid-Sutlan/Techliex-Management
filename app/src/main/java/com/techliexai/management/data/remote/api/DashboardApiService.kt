package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.DashboardStatsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface DashboardApiService {
    suspend fun getDashboardStats(): ApiResponse<DashboardStatsDto>
}

class DashboardApiServiceImpl(private val client: HttpClient) : DashboardApiService {
    override suspend fun getDashboardStats(): ApiResponse<DashboardStatsDto> {
        return client.get("/api/v1/dashboard/stats").body()
    }
}
