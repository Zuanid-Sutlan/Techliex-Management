package com.techliexai.management.presetation.utils

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.techliexai.management.presetation.components.enums.MessageType

fun copyToClipboard(
    clipboardManager: ClipboardManager,
    text: String,
    label: String = "Copied to clipboard"
) {
    if (text.isNotBlank()) {
        clipboardManager.setText(AnnotatedString(text))
        EventManager.showMessage(label, MessageType.SUCCESS)
    }
}
