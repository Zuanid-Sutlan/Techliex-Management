package com.techliexai.management.presetation.screen.members

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.User
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberScreenViewModel(private val context: Context) : ViewModel() {

    private val accountRef = Firebase.getAccountReference()

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
                if (!isInternetAvailable(context)) {
                    EventManager.showMessage("No internet connection", MessageType.ERROR)
                    EventManager.hideLoading()
                    return@launch
                }
                accountRef.addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val users =
                                snapshot.children.mapNotNull { it.getValue(User::class.java) }
                            _state.value = _state.value.copy(members = users)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            EventManager.showMessage(error.message, MessageType.ERROR)
                        }
                    }
                )
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
            }
        }
    }
}