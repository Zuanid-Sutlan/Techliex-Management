package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.DashboardApiService
import com.techliexai.management.data.remote.dto.DashboardStatsDto
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.repository.DashboardRepository

class DashboardRepositoryImpl(
    private val dashboardApiService: DashboardApiService
) : DashboardRepository {

    override suspend fun getDashboardStats(): Result<DashboardStatsDto, DataError> {
        return safeCall {
            val response = dashboardApiService.getDashboardStats()
            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "Failed to fetch dashboard stats")
            }
        }
    }
}
