package com.techliexai.management.presetation.screen.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.domain.repository.UserPreferencesRepository
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HuntProductViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val productRef = Firebase.getProductHuntReference()

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
            viewModelScope.launch {
                productRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val products = snapshot.children.mapNotNull {
                            it.getValue(ProductHunt::class.java)
                        }
                        val fList = products.filter {
                            it.shareWith.contains(currentUsername.value) || it.addedBy == currentUsername.value
                        }
                        _state.value = _state.value.copy(products = fList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        EventManager.showMessage(error.message, MessageType.ERROR)
                    }

                })
            }
        }
    }


}