package com.techliexai.management.domain.model

data class ProductHunt(
    val id: Int = 0,
    val addedByUserId: Int = -1,
    val addedBy: String = "",
    val title: String = "",
    val description: String = "",
    val productImage: String = "",
    val sourceLink: String = "",
    val sourcePrice: Int = 0,
    val referenceLink: String = "",
    val referencePrice: Int = 0,

    val shareWith: List<String> = emptyList(),

    val warehousePrice: Int = 0,
    val warehouseNote: String = ""
)
