package com.techliexai.management.domain.model

data class Order(
    val id: Int = -1,
    val date: String = "",

    val addedByUserId: Int = -1,
    val addedBy: String = "",

    val productHuntId: Int = -1,
    val productHuntTitle: String = "",

    val address: String = "",
    val productImage: String = "",
    val variationNote: String = "",
    val quantity: String = "",
    val listingPrice: String = "",
    val paymentImage: String = "",


    // added by admin
    val trackId: String = "",
    val company: String = "",

    val status: String = "Active"
)
