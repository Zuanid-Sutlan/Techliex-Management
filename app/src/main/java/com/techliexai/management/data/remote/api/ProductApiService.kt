package com.techliexai.management.data.remote.api

import com.techliexai.management.data.remote.dto.ApiResponse
import com.techliexai.management.data.remote.dto.CreateProductRequest
import com.techliexai.management.data.remote.dto.ProductDto
import com.techliexai.management.data.remote.dto.UpdateWarehouseRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface ProductApiService {
    suspend fun createProduct(request: CreateProductRequest): ApiResponse<ProductDto>
    suspend fun getProducts(): ApiResponse<List<ProductDto>>
    suspend fun getProductById(id: Long): ApiResponse<ProductDto>
    suspend fun deleteProduct(id: Long): ApiResponse<Unit>
    suspend fun updateWarehouse(id: Long, request: UpdateWarehouseRequest): ApiResponse<ProductDto>
}

class ProductApiServiceImpl(private val client: HttpClient) : ProductApiService {
    override suspend fun createProduct(request: CreateProductRequest): ApiResponse<ProductDto> {
        return client.post("/api/v1/products") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getProducts(): ApiResponse<List<ProductDto>> {
        return client.get("/api/v1/products").body()
    }

    override suspend fun getProductById(id: Long): ApiResponse<ProductDto> {
        return client.get("/api/v1/products/$id").body()
    }

    override suspend fun deleteProduct(id: Long): ApiResponse<Unit> {
        return client.delete("/api/v1/products/$id").body()
    }

    override suspend fun updateWarehouse(id: Long, request: UpdateWarehouseRequest): ApiResponse<ProductDto> {
        return client.patch("/api/v1/products/$id/warehouse") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
