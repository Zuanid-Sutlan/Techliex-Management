package com.techliexai.management.domain.repository

import com.techliexai.management.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): Result<List<Order>, DataError>
    suspend fun getOrderById(id: Int): Result<Order, DataError>
    suspend fun addOrder(order: Order): Result<String, DataError>
    suspend fun updateOrder(order: Order): Result<Unit, DataError>
    suspend fun deleteOrder(id: Int): Result<Unit, DataError>
}
