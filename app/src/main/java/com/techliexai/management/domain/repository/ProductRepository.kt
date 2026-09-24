package com.techliexai.management.domain.repository

import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.ProductHunt

interface ProductRepository {
    suspend fun createProduct(product: ProductHunt): Result<ProductHunt, DataError>
    suspend fun getProducts(): Result<List<ProductHunt>, DataError>
    suspend fun getProductById(id: Long): Result<ProductHunt, DataError>
    suspend fun deleteProduct(id: Long): Result<Unit, DataError>
    suspend fun updateWarehouse(id: Long, warehousePrice: Double, warehouseNote: String?): Result<ProductHunt, DataError>
}
