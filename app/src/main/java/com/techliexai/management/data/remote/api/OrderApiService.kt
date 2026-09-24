package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.CreateOrderRequest
import com.techliexai.management.data.remote.dto.OrderDto
import com.techliexai.management.data.remote.dto.UpdateLogisticsRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface OrderApiService {
    suspend fun createOrder(request: CreateOrderRequest): ApiResponse<OrderDto>
    suspend fun getOrders(): ApiResponse<List<OrderDto>>
    suspend fun getOrderById(id: Long): ApiResponse<OrderDto>
    suspend fun updateLogistics(id: Long, request: UpdateLogisticsRequest): ApiResponse<OrderDto>
    suspend fun completeOrder(id: Long): ApiResponse<OrderDto>
}

class OrderApiServiceImpl(private val client: HttpClient) : OrderApiService {
    override suspend fun createOrder(request: CreateOrderRequest): ApiResponse<OrderDto> {
        return client.post("/api/v1/orders") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getOrders(): ApiResponse<List<OrderDto>> {
        return client.get("/api/v1/orders").body()
    }

    override suspend fun getOrderById(id: Long): ApiResponse<OrderDto> {
        return client.get("/api/v1/orders/$id").body()
    }

    override suspend fun updateLogistics(id: Long, request: UpdateLogisticsRequest): ApiResponse<OrderDto> {
        return client.patch("/api/v1/orders/$id/logistics") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun completeOrder(id: Long): ApiResponse<OrderDto> {
        return client.patch("/api/v1/orders/$id/complete").body()
    }
}
