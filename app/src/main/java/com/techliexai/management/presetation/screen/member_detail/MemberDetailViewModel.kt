package com.techliexai.management.presetation.screen.member_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techliexai.management.data.database.Firebase
import com.techliexai.management.domain.model.User
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberDetailViewModel : ViewModel() {

    private val accountRef = Firebase.getAccountReference()

    private val _state = MutableStateFlow(MemberDetailScreeState())
    val state = _state.asStateFlow()

    fun onAction(action: MemberDetailAction) {
        when (action) {
            is MemberDetailAction.OnLoadMember -> {
                loadMemberDetails(action.username)
            }
            MemberDetailAction.OnBackClicked -> EventManager.navigateBack()
            MemberDetailAction.OnDeleteMemberClicked -> deleteMember()
            MemberDetailAction.OnEditMemberClicked -> {
//                editMember()
            }
        }
    }

    fun loadMemberDetails(username: String) {
        viewModelScope.launch {
            try {
                accountRef.child(username).get()
                    .addOnSuccessListener {
                        val user = it.getValue(User::class.java)
                        _state.value = _state.value.copy(user = user!!)
                    }
                    .addOnFailureListener {
                        EventManager.showMessage(it.message.toString(), MessageType.ERROR)
                    }
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
            }
        }
    }

    fun deleteMember(){
        viewModelScope.launch {
            EventManager.showLoading()
            try {
                accountRef.child(_state.value.user.username).removeValue()
                EventManager.showMessage("Member deleted successfully", MessageType.SUCCESS)
                EventManager.navigateBack()
                EventManager.hideLoading()
            } catch (e: Exception) {
                EventManager.showMessage(e.message.toString(), MessageType.ERROR)
                EventManager.hideLoading()
            }
        }
    }

}