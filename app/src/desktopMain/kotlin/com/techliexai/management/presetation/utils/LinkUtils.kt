package com.techliexai.management.presetation.utils

import java.awt.Desktop
import java.net.URI

actual fun openLink(url: String) {
    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(url))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
