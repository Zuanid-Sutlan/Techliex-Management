package com.techliexai.management.presetation.screen.products

import com.techliexai.management.domain.model.ProductHunt

sealed interface HuntProductScreenAction {
    object OnNavigateBackClicked : HuntProductScreenAction
    data class OnProductClicked(val product: ProductHunt) : HuntProductScreenAction
    data class OnSearchQueryChanged(val query: String) : HuntProductScreenAction
    object OnAddProductClicked : HuntProductScreenAction
}