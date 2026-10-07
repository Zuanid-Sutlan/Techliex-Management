package com.techliexai.management.data.repository

import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.repository.DataError
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.Result

class ProductRepositoryFake : ProductRepository {
    private val products = mutableMapOf<Int, ProductHunt>(
        1 to ProductHunt(
            id = 1,
            title = "Sample Product 1",
            description = "Description for product 1",
            sourcePrice = 100,
            referencePrice = 120,
            addedBy = "admin",
            shareWith = listOf("admin", "manager")
        )
    )

    override suspend fun getProducts(): Result<List<ProductHunt>, DataError> {
        return Result.Success(products.values.toList())
    }

    override suspend fun getProductById(id: Int): Result<ProductHunt, DataError> {
        val product = products[id]
        return if (product != null) Result.Success(product) else Result.Failure(DataError.ServerError)
    }

    override suspend fun addProduct(product: ProductHunt): Result<String, DataError> {
        val newId = (products.keys.maxOrNull() ?: 0) + 1
        val newProduct = product.copy(id = newId)
        products[newId] = newProduct
        return Result.Success("Product saved successfully")
    }

    override suspend fun updateProduct(product: ProductHunt): Result<Unit, DataError> {
        products[product.id] = product
        return Result.Success(Unit)
    }

    override suspend fun deleteProduct(id: Int): Result<Unit, DataError> {
        products.remove(id)
        return Result.Success(Unit)
    }
}
