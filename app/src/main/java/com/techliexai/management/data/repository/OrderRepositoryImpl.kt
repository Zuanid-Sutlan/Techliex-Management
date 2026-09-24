package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.OrderApiService
import com.techliexai.management.data.remote.dto.CreateOrderRequest
import com.techliexai.management.data.remote.dto.OrderDto
import com.techliexai.management.data.remote.dto.UpdateLogisticsRequest
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val orderApiService: OrderApiService
) : OrderRepository {

    override suspend fun createOrder(order: Order): Result<Order, DataError> {
        return safeCall {
            val request = CreateOrderRequest(
                orderDate = order.date,
                productId = order.productHuntId.toLong(),
                address = order.address,
                variationNote = order.variationNote,
                quantity = order.quantity.toIntOrNull() ?: 1,
                listingPrice = order.listingPrice.toDoubleOrNull() ?: 0.0,
                paymentImageUrl = order.paymentImage
            )
            val response = orderApiService.createOrder(request)
            if (response.success && response.data != null) {
                response.data.toDomainOrder()
            } else {
                throw Exception(response.message ?: "Failed to create order")
            }
        }
    }

    override suspend fun getOrders(): Result<List<Order>, DataError> {
        return safeCall {
            val response = orderApiService.getOrders()
            if (response.success && response.data != null) {
                response.data.map { it.toDomainOrder() }
            } else {
                throw Exception(response.message ?: "Failed to fetch orders")
            }
        }
    }

    override suspend fun getOrderById(id: Long): Result<Order, DataError> {
        return safeCall {
            val response = orderApiService.getOrderById(id)
            if (response.success && response.data != null) {
                response.data.toDomainOrder()
            } else {
                throw Exception(response.message ?: "Order not found")
            }
        }
    }

    override suspend fun updateLogistics(
        id: Long,
        trackId: String,
        company: String
    ): Result<Order, DataError> {
        return safeCall {
            val request = UpdateLogisticsRequest(trackId = trackId, company = company)
            val response = orderApiService.updateLogistics(id, request)
            if (response.success && response.data != null) {
                response.data.toDomainOrder()
            } else {
                throw Exception(response.message ?: "Failed to update logistics")
            }
        }
    }

    override suspend fun completeOrder(id: Long): Result<Order, DataError> {
        return safeCall {
            val response = orderApiService.completeOrder(id)
            if (response.success && response.data != null) {
                response.data.toDomainOrder()
            } else {
                throw Exception(response.message ?: "Failed to complete order")
            }
        }
    }
}

fun OrderDto.toDomainOrder(): Order {
    return Order(
        id = id.toInt(),
        date = orderDate ?: "",
        addedByUserId = userId?.toInt() ?: -1,
        addedBy = userName ?: "",
        productHuntId = productId?.toInt() ?: -1,
        productHuntTitle = productTitle ?: "",
        address = address ?: "",
        productImage = paymentImageUrl ?: "",
        variationNote = variationNote ?: "",
        quantity = quantity?.toString() ?: "1",
        listingPrice = listingPrice?.toString() ?: "0.0",
        paymentImage = paymentImageUrl ?: "",
        trackId = trackId ?: "",
        company = company ?: "",
        status = status ?: "Active"
    )
}
