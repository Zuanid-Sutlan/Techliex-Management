package com.techliexai.management.presetation.screen.orders_add

import com.techliexai.management.domain.model.ProductHunt

sealed interface AddOrderScreenAction {
    object OnNavigateBackClicked : AddOrderScreenAction
    object OnSaveClicked : AddOrderScreenAction

    data class OnProductSelected(val product: ProductHunt) : AddOrderScreenAction
    data class OnOrderDateChanged(val date: String) : AddOrderScreenAction
    data class OnAddressChanged(val address: String) : AddOrderScreenAction
    data class OnVariationNoteChanged(val note: String) : AddOrderScreenAction
    data class OnQuantityChanged(val quantity: String) : AddOrderScreenAction
    data class OnListingPriceChanged(val price: String) : AddOrderScreenAction
    data class OnPaymentImageChanged(val image: String) : AddOrderScreenAction


}