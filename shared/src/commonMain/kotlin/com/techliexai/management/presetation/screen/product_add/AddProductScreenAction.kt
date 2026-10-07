package com.techliexai.management.presetation.screen.product_add

import com.techliexai.management.domain.model.User

sealed interface AddProductScreenAction {
    data class OnTitleChanged(val title: String) : AddProductScreenAction
    data class OnNoteChanged(val description: String) : AddProductScreenAction
    data class OnProductImageChanged(val productImage: String) : AddProductScreenAction
    data class OnSourceLinkChanged(val sourceLink: String) : AddProductScreenAction
    data class OnSourcePriceChanged(val sourcePrice: Int) : AddProductScreenAction
    data class OnReferenceLinkChanged(val referenceLink: String) : AddProductScreenAction
    data class OnReferencePriceChanged(val referencePrice: Int) : AddProductScreenAction
    data class OnShareWithChanged(val shareWith: List<User>) : AddProductScreenAction

    object OnSaveClicked : AddProductScreenAction
    object OnNavigateBackClicked : AddProductScreenAction

}