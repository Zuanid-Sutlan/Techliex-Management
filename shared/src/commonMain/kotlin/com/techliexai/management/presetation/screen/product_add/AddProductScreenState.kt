package com.techliexai.management.presetation.screen.product_add

import com.techliexai.management.domain.model.User

data class AddProductScreenState(
    val title: String = "",
    val note: String = "",
    val productImage: String = "",
    val sourceLink: String = "",
    val sourcePrice: Int = 0,
    val referenceLink: String = "",
    val referencePrice: Int = 0,

    val members: List<User> = emptyList(),
    val shareWith: List<User> = emptyList(),


)
