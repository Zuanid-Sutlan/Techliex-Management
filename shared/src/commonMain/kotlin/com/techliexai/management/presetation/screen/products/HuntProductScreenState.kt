package com.techliexai.management.presetation.screen.products

import com.techliexai.management.domain.model.ProductHunt

data class HuntProductScreenState(
    val products: List<ProductHunt> = emptyList(),
    val searchQuery: String = ""
)
