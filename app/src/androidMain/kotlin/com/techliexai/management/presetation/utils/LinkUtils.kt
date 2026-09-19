package com.techliexai.management.presetation.utils

import android.content.Intent
import android.net.Uri

actual fun openLink(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AndroidContextProvider.context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
