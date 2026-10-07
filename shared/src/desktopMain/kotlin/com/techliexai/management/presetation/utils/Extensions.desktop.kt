package com.techliexai.management.presetation.utils

import java.awt.Desktop
import java.net.URI

actual fun openWhatsAppChat(phoneNumber: String) {
    openUrl("https://wa.me/$phoneNumber")
}

actual fun openUrl(url: String) {
    try {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
