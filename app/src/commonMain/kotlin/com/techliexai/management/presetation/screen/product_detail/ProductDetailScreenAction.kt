package com.techliexai.management.presetation.screen.product_detail

sealed interface ProductDetailScreenAction {
    object OnNavigateBackClicked : ProductDetailScreenAction
    data class OnLoadProduct(val productId: Int) : ProductDetailScreenAction
    object OnEditClicked : ProductDetailScreenAction
    object OnDeleteClicked : ProductDetailScreenAction

    data class OnWarehousePriceChanged(val price: Int) : ProductDetailScreenAction
    data class OnWarehouseNoteChanged(val note: String) : ProductDetailScreenAction

    object OnSaveWarehouseDetailsClicked : ProductDetailScreenAction


}