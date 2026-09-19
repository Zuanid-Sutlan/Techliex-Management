package com.techliexai.management.presetation.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun openWhatsAppChat(phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val context = AndroidContextProvider.context
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

actual fun isInternetAvailable(): Boolean {
    val connectivityManager = AndroidContextProvider.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

object AndroidContextProvider : KoinComponent {
    val context: Context by inject()
}
