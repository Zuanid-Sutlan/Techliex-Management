package com.techliexai.management.domain.repository

import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.Order

interface OrderRepository {
    suspend fun createOrder(order: Order): Result<Order, DataError>
    suspend fun getOrders(): Result<List<Order>, DataError>
    suspend fun getOrderById(id: Long): Result<Order, DataError>
    suspend fun updateLogistics(id: Long, trackId: String, company: String): Result<Order, DataError>
    suspend fun completeOrder(id: Long): Result<Order, DataError>
}
