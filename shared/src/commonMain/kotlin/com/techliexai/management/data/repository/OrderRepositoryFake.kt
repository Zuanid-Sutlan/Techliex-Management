package com.techliexai.management.data.repository

import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.repository.DataError
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.Result

class OrderRepositoryFake : OrderRepository {
    private val orders = mutableMapOf<Int, Order>(
        1 to Order(
            id = 1,
            productHuntId = 1,
            productHuntTitle = "Sample Product 1",
            address = "123 Main St",
            quantity = "2",
            listingPrice = "120",
            status = "Active",
            addedBy = "Admin User"
        )
    )

    override suspend fun getOrders(): Result<List<Order>, DataError> {
        return Result.Success(orders.values.toList())
    }

    override suspend fun getOrderById(id: Int): Result<Order, DataError> {
        val order = orders[id]
        return if (order != null) Result.Success(order) else Result.Failure(DataError.ServerError)
    }

    override suspend fun addOrder(order: Order): Result<String, DataError> {
        val newId = (orders.keys.maxOrNull() ?: 0) + 1
        val newOrder = order.copy(id = newId)
        orders[newId] = newOrder
        return Result.Success("Order placed successfully!")
    }

    override suspend fun updateOrder(order: Order): Result<Unit, DataError> {
        orders[order.id] = order
        return Result.Success(Unit)
    }

    override suspend fun deleteOrder(id: Int): Result<Unit, DataError> {
        orders.remove(id)
        return Result.Success(Unit)
    }
}
