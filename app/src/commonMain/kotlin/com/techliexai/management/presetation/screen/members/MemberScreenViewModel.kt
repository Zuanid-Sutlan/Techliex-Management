package com.techliexai.management.presetation.screen.members

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.FirebaseDatabase
import com.techliexai.management.domain.model.User
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberScreenViewModel(private val context: Context) : ViewModel() {

    private val accountRef = FirebaseDatabase.getAccountReference()

    private val _state = MutableStateFlow(MemberState())
    val state = _state.asStateFlow()


    init {
        fetchAllUsers()
    }

    fun onAction(action: MembersAction){
        when(action){
            MembersAction.OnNavigateBackClicked -> {
                EventManager.navigateBack()
            }
            is MembersAction.OnMemberClicked -> {
                EventManager.navigateTo(screen = Screen.MemberDetailScreen(action.member.username))
            }
            MembersAction.OnAddMemberClicked -> {
                EventManager.navigateTo(screen = Screen.CreateMemberScreen)
            }
        }
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            try {
                if (!isInternetAvailable()) {
                    EventManager.showMessage("No internet connection", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                accountRef.snapshots.collect { snapshot ->
                    val users = snapshot.documents.map { it.data<User>() }
                    _state.value = _state.value.copy(members = users)
                }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
            }
        }
    }
}