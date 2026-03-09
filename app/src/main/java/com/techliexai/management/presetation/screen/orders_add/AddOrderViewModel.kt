package com.techliexai.management.presetation.screen.orders_add

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.model.User
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddOrderViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    private val productRef = Firebase.getProductHuntReference()
    private val orderRef = Firebase.getOrderReference()

    private val _state = MutableStateFlow(AddOrderScreeState())
    val state = _state.asStateFlow()

    init {
        fetchProducts()
    }

    fun onAction(action: AddOrderScreenAction){
        when(action){
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
            viewModelScope.launch {
                productRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val products = snapshot.children.mapNotNull {
                            it.getValue(ProductHunt::class.java)
                        }
                        val fList = products.filter {
                            it.shareWith.contains(currentUsername.value) || it.addedBy == currentUsername.value
                        }
                        _state.value = _state.value.copy(productList = fList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        EventManager.showMessage(error.message, MessageType.ERROR)
                    }

                })
            }
        }
    }

    private fun saveOrder() {
        viewModelScope.launch {
            val currentState = _state.value
            val mUser = mutableStateOf(User())

            // 1. Validation Logic
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

            // 2. Fetch current user info for "addedBy" fields
            viewModelScope.launch {
                userPreferencesRepository.getUser().collect { user ->
//                val orderId = System.currentTimeMillis().toInt() // Unique ID based on timestamp
                    mUser.value = user
                }
            }

            viewModelScope.launch {
                val id = orderRef.get().await().childrenCount.toInt() + 1

                val newOrder = Order(
                    id = id,
//                    date = currentState.orderDate.ifBlank {
//                        SimpleDateFormat(
//                            "dd/MM/yyyy",
//                            Locale.getDefault()
//                        ).format(Date())
//                    },
                    date = currentState.orderDate,
                    addedByUserId = mUser.value.id,
                    addedBy = mUser.value.name,
                    productHuntId = currentState.selectedProduct.id,
                    productHuntTitle = currentState.selectedProduct.title,
                    address = currentState.address,
                    productImage = currentState.selectedProduct.productImage, // Store product image for quick reference
                    variationNote = currentState.variationNote,
                    quantity = currentState.quantity,
                    listingPrice = currentState.listingPrice,
                    paymentImage = currentState.paymentImage,
                    status = "Active",
                    trackId = "", // Initially empty, added by Admin later
                    company = ""  // Initially empty, added by Admin later
                )

                // 3. Push to Firebase
                orderRef.child("Order_$id").setValue(newOrder)
                    .addOnSuccessListener {
                        EventManager.hideLoading()
                        EventManager.showMessage("Order placed successfully!", MessageType.SUCCESS)
                        EventManager.navigateBack()
                    }
                    .addOnFailureListener { error ->
                        EventManager.hideLoading()
                        EventManager.showMessage("Failed to save order: ${error.message}", MessageType.ERROR)
                    }
            }
        }
    }
}