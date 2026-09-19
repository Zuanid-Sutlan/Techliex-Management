package com.techliexai.management.presetation.screen.product_detail

import com.techliexai.management.domain.model.ProductHunt

data class ProductDetailScreenState(
    val product: ProductHunt? = ProductHunt(),
    val isAdmin: Boolean = false,

    val warehousePrice : Int = 0,
    val warehouseNote : String = ""
)
