package com.techliexai.management.presetation.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import org.koin.java.KoinJavaComponent.getKoin

actual fun openWhatsAppChat(phoneNumber: String) {
    openUrl("https://wa.me/$phoneNumber")
}

actual fun openUrl(url: String) {
    try {
        val context = getKoin().get<Context>()
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
