package com.techliexai.management.presetation.utils

import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.components.enums.MessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object EventManager {
    sealed class AppEvent {
        data class ShowToast(val message: String, val type: MessageType) : AppEvent()
        data class LoadingState(val isLoading: Boolean) : AppEvent()
        data class NavigateTo(val screen: Screen) : AppEvent()
        object NavigateBack : AppEvent()
    }

    private val _eventFlow = MutableSharedFlow<AppEvent>(replay = 0)
    val eventFlow: SharedFlow<AppEvent> = _eventFlow.asSharedFlow()

    suspend fun sendEvent(event: AppEvent) {
        _eventFlow.emit(event)
    }

    fun showMessage(message: String, type: MessageType) {
        CoroutineScope(Dispatchers.Main).launch {
            _eventFlow.emit(AppEvent.ShowToast(message, type))
        }
    }

    fun showLoading() {
        CoroutineScope(Dispatchers.Main).launch {
            _eventFlow.emit(AppEvent.LoadingState(true))
        }
    }

    fun hideLoading() {
        CoroutineScope(Dispatchers.Main).launch {
            _eventFlow.emit(AppEvent.LoadingState(false))
        }
    }

    fun navigateTo(screen: Screen) {
        CoroutineScope(Dispatchers.Main).launch {
            _eventFlow.emit(AppEvent.NavigateTo(screen))
        }
    }

    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _eventFlow.emit(AppEvent.NavigateBack)
        }
    }
}
