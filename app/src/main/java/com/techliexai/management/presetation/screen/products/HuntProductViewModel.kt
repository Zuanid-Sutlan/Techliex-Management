package com.techliexai.management.presetation.screen.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.utils.DataError
import com.techliexai.management.data.utils.Result
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HuntProductViewModel(
    private val productRepository: ProductRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HuntProductScreenState())
    val state = _state.asStateFlow()

    init {
        fetchUsersProducts()
    }

    fun onAction(action: HuntProductScreenAction) {
        when (action) {
            is HuntProductScreenAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }

            is HuntProductScreenAction.OnProductClicked -> {
                EventManager.navigateTo(Screen.ProductDetailScreen(action.product.id))
            }

            is HuntProductScreenAction.OnSearchQueryChanged -> {
                _state.value = _state.value.copy(searchQuery = action.query)
            }

            is HuntProductScreenAction.OnAddProductClicked -> {
                EventManager.navigateTo(Screen.AddProductScreen)
            }
        }
    }

    fun fetchUsersProducts() {
        viewModelScope.launch {
            val currentUser = userPreferencesRepository.getUser().firstOrNull()
            val currentUsername = currentUser?.username ?: ""
            val userRole = currentUser?.role ?: ""

            when (val result = productRepository.getProducts()) {
                is Result.Success -> {
                    val products = result.data
                    val filteredList = if (userRole == "Admin" || userRole == "Warehouse") {
                        products
                    } else {
                        products.filter {
                            it.shareWith.contains(currentUsername) || it.addedBy == currentUsername
                        }
                    }
                    _state.value = _state.value.copy(products = filteredList)
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
}
