package com.techliexai.management.presetation.screen.product_add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class AddProductViewModel(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val productRef = FirebaseDatabase.getProductHuntReference()
    private val accountRef = FirebaseDatabase.getAccountReference()

    private val _state = MutableStateFlow(AddProductScreenState())
    val state = _state.asStateFlow()

    init {
        fetchUsers()
    }

    fun onAction(action: AddProductScreenAction) {
        when (action) {
            is AddProductScreenAction.OnTitleChanged -> {
                _state.value = _state.value.copy(title = action.title)
            }

            is AddProductScreenAction.OnNoteChanged -> {
                _state.value = _state.value.copy(note = action.description)
            }

            is AddProductScreenAction.OnProductImageChanged -> {
                _state.value = _state.value.copy(productImage = action.productImage)
            }

            is AddProductScreenAction.OnSourceLinkChanged -> {
                _state.value = _state.value.copy(sourceLink = action.sourceLink)
            }

            is AddProductScreenAction.OnSourcePriceChanged -> {
                _state.value = _state.value.copy(sourcePrice = action.sourcePrice)
            }

            is AddProductScreenAction.OnReferenceLinkChanged -> {
                _state.value = _state.value.copy(referenceLink = action.referenceLink)
            }

            is AddProductScreenAction.OnReferencePriceChanged -> {
                _state.value = _state.value.copy(referencePrice = action.referencePrice)
            }

            is AddProductScreenAction.OnShareWithChanged -> {
                _state.value = _state.value.copy(shareWith = action.shareWith)
            }

            is AddProductScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is AddProductScreenAction.OnSaveClicked -> {
                saveProduct()
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val snapshot = accountRef.get()
                // Fetch users
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun saveProduct() {
        viewModelScope.launch {

            // validation fields
            if (_state.value.title.isEmpty() || _state.value.note.isEmpty() || _state.value.productImage.isEmpty() ||
                _state.value.sourceLink.isEmpty() || _state.value.sourcePrice == 0 ||
                _state.value.referenceLink.isEmpty() || _state.value.referencePrice == 0
            ) {
                EventManager.showMessage("Please fill all the fields", MessageType.ERROR)
                return@launch
            }

            EventManager.showLoading()

            try {
                val id = 100 // dummy id for now
                val product = ProductHunt(
                    id = id,
                    title = _state.value.title,
                    description = _state.value.note,
                    productImage = _state.value.productImage,
                    sourceLink = _state.value.sourceLink,
                    sourcePrice = _state.value.sourcePrice,
                    referenceLink = _state.value.referenceLink,
                    referencePrice = _state.value.referencePrice,
                    addedByUserId = userPreferencesRepository.getUser().firstOrNull()?.id
                        ?: -1,
                    addedBy = userPreferencesRepository.getUser().firstOrNull()?.username
                        ?: "anonymous",
                    shareWith = _state.value.shareWith.map { it.username }
                )
                productRef.document("Product_$id").set(product)
                EventManager.showMessage("Product saved successfully", MessageType.SUCCESS)
                EventManager.navigateBack()
                EventManager.hideLoading()
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
                EventManager.hideLoading()
            }
        }
    }

}