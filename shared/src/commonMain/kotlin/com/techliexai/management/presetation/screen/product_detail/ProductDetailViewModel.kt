package com.techliexai.management.presetation.screen.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.Result
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailScreenState())
    val state = _state.asStateFlow()

    init {
        checkIsAdmin()
    }

    fun onAction(action: ProductDetailScreenAction) {
        when (action) {
            is ProductDetailScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }
            is ProductDetailScreenAction.OnLoadProduct -> {
                onLoadProduct(productId = action.productId)
            }
            is ProductDetailScreenAction.OnEditClicked -> {
            }
            is ProductDetailScreenAction.OnWarehousePriceChanged -> {
                _state.value = _state.value.copy(warehousePrice = action.price)
            }
            is ProductDetailScreenAction.OnWarehouseNoteChanged -> {
                _state.value = _state.value.copy(warehouseNote = action.note)
            }
            is ProductDetailScreenAction.OnDeleteClicked -> {
                deleteProduct()
            }
            is ProductDetailScreenAction.OnSaveWarehouseDetailsClicked -> {
                updateWarehouseDetails()
            }
        }
    }

    private fun onLoadProduct(productId: Int) {
        viewModelScope.launch {
            EventManager.showLoading()
            when (val result = productRepository.getProductById(productId)) {
                is Result.Success -> {
                    _state.value = _state.value.copy(product = result.data)
                }
                is Result.Failure -> {
                    EventManager.showMessage("Product not found", MessageType.ERROR)
                }
            }
            EventManager.hideLoading()
        }
    }

    private fun updateWarehouseDetails() {
        viewModelScope.launch {
            val currentState = _state.value
            val product = currentState.product
            if (product == null || product.id <= 0) {
                EventManager.showMessage("Invalid Product ID", MessageType.ERROR)
                return@launch
            }

            EventManager.showLoading()
            val updatedProduct = product.copy(
                warehousePrice = currentState.warehousePrice,
                warehouseNote = currentState.warehouseNote
            )

            when (val result = productRepository.updateProduct(updatedProduct)) {
                is Result.Success -> {
                    EventManager.showMessage("Warehouse data updated successfully", MessageType.SUCCESS)
                    _state.value = currentState.copy(product = updatedProduct)
                }
                is Result.Failure -> {
                    EventManager.showMessage("Update failed", MessageType.ERROR)
                }
            }
            EventManager.hideLoading()
        }
    }

    private fun deleteProduct() {
        val currentState = _state.value
        val product = currentState.product
        if (product == null || product.id <= 0) {
            EventManager.showMessage("Error: Invalid Product ID", MessageType.ERROR)
            return
        }

        viewModelScope.launch {
            EventManager.showLoading()
            when (val result = productRepository.deleteProduct(product.id)) {
                is Result.Success -> {
                    EventManager.showMessage("Product removed successfully", MessageType.SUCCESS)
                    EventManager.navigateBack()
                }
                is Result.Failure -> {
                    EventManager.showMessage("Delete failed", MessageType.ERROR)
                }
            }
            EventManager.hideLoading()
        }
    }

    fun checkIsAdmin() {
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(isAdmin = it.role == "Admin")
            }
        }
    }
}
