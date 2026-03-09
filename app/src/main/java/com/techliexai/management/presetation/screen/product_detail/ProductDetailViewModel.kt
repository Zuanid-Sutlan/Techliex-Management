package com.techliexai.management.presetation.screen.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    private val productRef = Firebase.getProductHuntReference()

    private val _state = MutableStateFlow(ProductDetailScreenState())
    val state = _state.asStateFlow()

    init {
        checkIsAdmin()
    }

    fun onAction(action: ProductDetailScreenAction){
        when(action){
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


    private fun onLoadProduct(productId: Int){
        viewModelScope.launch {
            EventManager.showLoading()
            productRef.child("Product_$productId").get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val product = it.getValue(ProductHunt::class.java)
                        _state.value = _state.value.copy(product = product!!)
                    }
                    EventManager.hideLoading()
                }
                .addOnFailureListener {
                    EventManager.showMessage(it.message.toString(), MessageType.ERROR)
                    EventManager.hideLoading()
                }
        }
    }

    private fun updateWarehouseDetails() {
        viewModelScope.launch {
            val currentState = _state.value
            val productId = currentState.product?.id

            // Validation: Ensure we have a valid product ID before updating
            if (productId == 0) {
                EventManager.showMessage("Invalid Product ID", MessageType.ERROR)
                return@launch
            }

            EventManager.showLoading()

            // Create a map of the specific fields to update
            val updates = mapOf(
                "warehousePrice" to currentState.warehousePrice,
                "warehouseNote" to currentState.warehouseNote
            )

            productRef.child("Product_$productId").updateChildren(updates)
                .addOnSuccessListener {
                    EventManager.showMessage("Warehouse data updated successfully", MessageType.SUCCESS)
                    EventManager.hideLoading()

                    // Optional: Refresh the local product state to reflect changes
                    val updatedProduct = currentState.product?.copy(
                        warehousePrice = currentState.warehousePrice,
                        warehouseNote = currentState.warehouseNote
                    )
                    _state.value = currentState.copy(product = updatedProduct)
                }
                .addOnFailureListener {
                    EventManager.showMessage(it.message ?: "Update failed", MessageType.ERROR)
                    EventManager.hideLoading()
                }
        }
    }

    private fun deleteProduct() {

        val currentState = _state.value
        val productId = currentState.product?.id

        // 1. Guard clause: Prevent deletion of invalid IDs
        if (productId != null) {
            if (productId <= 0) {
                EventManager.showMessage("Error: Invalid Product ID", MessageType.ERROR)
                return
            }
        }

        viewModelScope.launch {
            EventManager.showLoading()

            // 2. Reference the specific node: Product_X
            productRef.child("Product_$productId").removeValue()
                .addOnSuccessListener {
                    EventManager.showMessage("Product removed successfully", MessageType.SUCCESS)

                    // 3. Close loading and exit the detail screen
                    EventManager.hideLoading()
                    EventManager.navigateBack()
                }
                .addOnFailureListener { exception ->
                    // 4. Handle network or permission errors
                    val errorMessage = exception.message ?: "An unknown error occurred"
                    EventManager.showMessage("Delete failed: $errorMessage", MessageType.ERROR)
                    EventManager.hideLoading()
                }
        }
    }

    fun checkIsAdmin(){
        viewModelScope.launch {
            userPreferencesRepository.getUser().collect {
                _state.value = _state.value.copy(isAdmin = it.role == "Admin")
            }
        }
    }

}