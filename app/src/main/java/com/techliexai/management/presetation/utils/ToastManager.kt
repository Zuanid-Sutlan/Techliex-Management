package com.techliexai.management.presetation.utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

object ToastManager {

    data class MessageEvent(val message: String)

    private val toastChannel = Channel<MessageEvent>(Channel.BUFFERED)

    // A flow to emit Toast events
    @OptIn(DelicateCoroutinesApi::class)
    val toastFlow: StateFlow<MessageEvent?> = toastChannel.receiveAsFlow().stateIn(
        scope = GlobalScope, // This is just an example, use appropriate scope like ViewModelScope in actual app
        started = SharingStarted.Lazily,
        initialValue = null
    )

    // Function to send a toast message
    fun sendToastMessage(message: String) {
        toastChannel.trySend(MessageEvent(message)).isSuccess
    }
}
