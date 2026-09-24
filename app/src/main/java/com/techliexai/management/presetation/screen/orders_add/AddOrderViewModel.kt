package com.techliexai.management.presetation.screen.orders_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.MediaRepository
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AddOrderViewModel(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val mediaRepository: MediaRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddOrderScreeState())
    val state = _state.asStateFlow()

    init {
        fetchProducts()
    }

    fun onAction(action: AddOrderScreenAction) {
        when (action) {
            is AddOrderScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is AddOrderScreenAction.OnOrderDateChanged -> {
                _state.value = _state.value.copy(orderDate = action.date)
            }

            is AddOrderScreenAction.OnAddressChanged -> {
                _state.value = _state.value.copy(address = action.address)
            }

            is AddOrderScreenAction.OnVariationNoteChanged -> {
                _state.value = _state.value.copy(variationNote = action.note)
            }

            is AddOrderScreenAction.OnQuantityChanged -> {
                _state.value = _state.value.copy(quantity = action.quantity)
            }

            is AddOrderScreenAction.OnListingPriceChanged -> {
                _state.value = _state.value.copy(listingPrice = action.price)
            }

            is AddOrderScreenAction.OnProductSelected -> {
                _state.value = _state.value.copy(selectedProduct = action.product)
            }

            is AddOrderScreenAction.OnPaymentImageChanged -> {
                _state.value = _state.value.copy(paymentImage = action.image)
            }

            is AddOrderScreenAction.OnSaveClicked -> {
                saveOrder()
            }
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            val currentUser = userPreferencesRepository.getUser().firstOrNull()
            val currentUsername = currentUser?.username ?: ""

            when (val result = productRepository.getProducts()) {
                is Result.Success -> {
                    val products = result.data
                    val filteredList = products.filter {
                        it.shareWith.contains(currentUsername) || it.addedBy == currentUsername
                    }
                    _state.value = _state.value.copy(productList = filteredList)
                }

                is Result.Failure -> {
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Failed to fetch products"
                        else -> "Failed to fetch products"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }

    private fun saveOrder() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.selectedProduct == null) {
                EventManager.showMessage("Please select a product first", MessageType.ERROR)
                return@launch
            }
            if (currentState.address.isBlank() || currentState.quantity.isBlank()) {
                EventManager.showMessage("Address and Quantity are required", MessageType.ERROR)
                return@launch
            }
            if (currentState.paymentImage.isBlank()) {
                EventManager.showMessage("Please upload payment proof", MessageType.ERROR)
                return@launch
            }

            EventManager.showLoading()

            val currentUser = userPreferencesRepository.getUser().firstOrNull() ?: User()

            val newOrder = Order(
                date = currentState.orderDate,
                addedByUserId = currentUser.id,
                addedBy = currentUser.name,
                productHuntId = currentState.selectedProduct.id,
                productHuntTitle = currentState.selectedProduct.title,
                address = currentState.address,
                productImage = currentState.selectedProduct.productImage,
                variationNote = currentState.variationNote,
                quantity = currentState.quantity,
                listingPrice = currentState.listingPrice,
                paymentImage = currentState.paymentImage,
                status = "Active",
                trackId = "",
                company = ""
            )

            when (val result = orderRepository.createOrder(newOrder)) {
                is Result.Success -> {
                    EventManager.hideLoading()
                    EventManager.showMessage("Order placed successfully!", MessageType.SUCCESS)
                    EventManager.navigateBack()
                }

                is Result.Failure -> {
                    EventManager.hideLoading()
                    val errorMsg = when (val error = result.error) {
                        is DataError.Unknown -> error.message ?: "Failed to save order"
                        else -> "Failed to save order"
                    }
                    EventManager.showMessage(errorMsg, MessageType.ERROR)
                }
            }
        }
    }
}
