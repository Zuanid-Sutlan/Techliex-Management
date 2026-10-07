package com.techliexai.management.presetation.screen.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.domain.repository.ProductRepository
import com.techliexai.management.domain.repository.Result
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                    _state.value = _state.value.copy(products = fList)
                }
                is Result.Failure -> {
                    EventManager.showMessage("Failed to load products", MessageType.ERROR)
                }
            }
        }
    }
}
