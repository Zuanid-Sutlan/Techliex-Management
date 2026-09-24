package com.techliexai.management.domain.repository

import com.techliexai.management.data.remote.dto.DashboardStatsDto
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result

interface DashboardRepository {
    suspend fun getDashboardStats(): Result<DashboardStatsDto, DataError>
}
