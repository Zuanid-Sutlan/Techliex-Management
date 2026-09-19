package com.techliexai.management.presetation.screen.orders_add

import com.techliexai.management.domain.model.ProductHunt

data class AddOrderScreeState(
    val productList: List<ProductHunt> = emptyList(),
    val selectedProduct: ProductHunt? = null,
    val orderDate: String = "",
    val address: String = "",
    val variationNote: String = "",
    val quantity: String = "",
    val listingPrice: String = "",
    val paymentImage: String = ""

)