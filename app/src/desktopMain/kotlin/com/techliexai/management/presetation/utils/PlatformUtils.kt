package com.techliexai.management.presetation.utils

import java.awt.Desktop
import java.net.URI
import java.net.InetAddress

actual fun openWhatsAppChat(phoneNumber: String) {
    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI("https://api.whatsapp.com/send?phone=$phoneNumber"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

actual fun isInternetAvailable(): Boolean {
    return try {
        val address = InetAddress.getByName("8.8.8.8")
        address.isReachable(3000)
    } catch (e: Exception) {
        false
    }
}
