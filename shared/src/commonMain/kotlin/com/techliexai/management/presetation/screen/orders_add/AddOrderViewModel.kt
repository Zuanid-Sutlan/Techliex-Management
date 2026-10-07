package com.techliexai.management.presetation.screen.orders_add

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.OrderRepository
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.Result
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
            val currentUsername = mutableStateOf("")
            viewModelScope.launch {
                userPreferencesRepository.getUser().collect {
                    currentUsername.value = it.username
                }
            }
            when (val result = productRepository.getProducts()) {
                is Result.Success -> {
                    val products = result.data
                    val fList = products.filter {
                        it.shareWith.contains(currentUsername.value) || it.addedBy == currentUsername.value
                    }
                    _state.value = _state.value.copy(productList = fList)
                }
                is Result.Failure -> {}
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

            val mUser = userPreferencesRepository.getUser().firstOrNull() ?: User()

            val newOrder = Order(
                date = currentState.orderDate,
                addedByUserId = mUser.id,
                addedBy = mUser.name,
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

            when (val result = orderRepository.addOrder(newOrder)) {
                is Result.Success -> {
                    EventManager.hideLoading()
                    EventManager.showMessage(result.data, MessageType.SUCCESS)
                    EventManager.navigateBack()
                }
                is Result.Failure -> {
                    EventManager.hideLoading()
                    EventManager.showMessage("Failed to save order", MessageType.ERROR)
                }
            }
        }
    }
}
