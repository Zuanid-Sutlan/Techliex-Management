package com.techliexai.management.domain.repository

import com.techliexai.management.domain.model.ProductHunt

interface ProductRepository {
    suspend fun getProducts(): Result<List<ProductHunt>, DataError>
    suspend fun getProductById(id: Int): Result<ProductHunt, DataError>
    suspend fun addProduct(product: ProductHunt): Result<String, DataError>
    suspend fun updateProduct(product: ProductHunt): Result<Unit, DataError>
    suspend fun deleteProduct(id: Int): Result<Unit, DataError>
}
