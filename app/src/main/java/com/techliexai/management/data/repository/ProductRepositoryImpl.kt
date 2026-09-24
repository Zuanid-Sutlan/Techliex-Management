package com.techliexai.management.data.repository

import com.techliexai.management.data.remote.api.ProductApiService
import com.techliexai.management.data.remote.dto.CreateProductRequest
import com.techliexai.management.data.remote.dto.ProductDto
import com.techliexai.management.data.remote.dto.UpdateWarehouseRequest
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.data.utils.safeCall
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productApiService: ProductApiService
) : ProductRepository {

    override suspend fun createProduct(product: ProductHunt): Result<ProductHunt, DataError> {
        return safeCall {
            val request = CreateProductRequest(
                title = product.title,
                description = product.description,
                productImageUrl = product.productImage,
                sourceLink = product.sourceLink,
                sourcePrice = product.sourcePrice.toDouble(),
                referenceLink = product.referenceLink,
                referencePrice = product.referencePrice.toDouble(),
                shareWithUsernames = product.shareWith
            )
            val response = productApiService.createProduct(request)
            if (response.success && response.data != null) {
                response.data.toDomainProduct()
            } else {
                throw Exception(response.message ?: "Failed to create product")
            }
        }
    }

    override suspend fun getProducts(): Result<List<ProductHunt>, DataError> {
        return safeCall {
            val response = productApiService.getProducts()
            if (response.success && response.data != null) {
                response.data.map { it.toDomainProduct() }
            } else {
                throw Exception(response.message ?: "Failed to fetch products")
            }
        }
    }

    override suspend fun getProductById(id: Long): Result<ProductHunt, DataError> {
        return safeCall {
            val response = productApiService.getProductById(id)
            if (response.success && response.data != null) {
                response.data.toDomainProduct()
            } else {
                throw Exception(response.message ?: "Product not found")
            }
        }
    }

    override suspend fun deleteProduct(id: Long): Result<Unit, DataError> {
        return safeCall {
            val response = productApiService.deleteProduct(id)
            if (response.success) {
                Unit
            } else {
                throw Exception(response.message ?: "Failed to delete product")
            }
        }
    }

    override suspend fun updateWarehouse(
        id: Long,
        warehousePrice: Double,
        warehouseNote: String?
    ): Result<ProductHunt, DataError> {
        return safeCall {
            val request = UpdateWarehouseRequest(
                warehousePrice = warehousePrice,
                warehouseNote = warehouseNote
            )
            val response = productApiService.updateWarehouse(id, request)
            if (response.success && response.data != null) {
                response.data.toDomainProduct()
            } else {
                throw Exception(response.message ?: "Failed to update warehouse details")
            }
        }
    }
}

fun ProductDto.toDomainProduct(): ProductHunt {
    return ProductHunt(
        id = id.toInt(),
        addedByUserId = userId?.toInt() ?: -1,
        addedBy = creatorName ?: "",
        title = title,
        description = description ?: "",
        productImage = productImageUrl,
        sourceLink = sourceLink,
        sourcePrice = sourcePrice.toInt(),
        referenceLink = referenceLink,
        referencePrice = referencePrice.toInt(),
        shareWith = sharedUsernames,
        warehousePrice = warehousePrice?.toInt() ?: 0,
        warehouseNote = warehouseNote ?: ""
    )
}
