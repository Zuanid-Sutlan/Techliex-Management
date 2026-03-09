package com.techliexai.management.presetation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.techliexai.management.presetation.components.enums.MessageType
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder
import androidx.core.net.toUri

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // For Android 10 (API level 29) and above, use NetworkCapabilities
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        // For lower versions, use getAllNetworkInfo (deprecated but works for now)
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnected == true
    }
}

fun Bitmap.toBase64(): String {
    val outputStream = ByteArrayOutputStream()
    // Using WEBP_LOSSY or JPEG at 80-90% for high quality with balanced string length
    this.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    val bytes = outputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String.toBitmap(): Bitmap? {
    return try {
        val imageBytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        null // Return null if the string is not valid Base64 or is corrupted
    }
}


fun copyToClipboard(
    clipboardManager: ClipboardManager,
    text: String,
    label: String = "Copied to clipboard"
) {
    if (text.isNotBlank()) {
        clipboardManager.setText(AnnotatedString(text))
        // Use your existing EventManager to show a quick success toast
        EventManager.showMessage(label, MessageType.SUCCESS)
    }
}

fun openWhatsAppChat(context: Context, phoneNumber: String) {
    /*// 1. Format the number: remove any non-digit characters
    val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")

    // 2. Build the URL (wa.me is the official WhatsApp short link)
    val url = "https://wa.me{URLEncoder.encode(message, ${"UTF-8"})}"

    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            // Optional: Explicitly target WhatsApp to bypass the browser chooser
            setPackage("com.whatsapp")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback: If WhatsApp is not installed, open in a browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
        Toast.makeText(context, "WhatsApp not installed. Opening in browser...", Toast.LENGTH_SHORT).show()
    }*/
    try {
        val intent = run {
            // Open chat with specific number
            val url = "https://wa.me/$phoneNumber"
            Intent(Intent.ACTION_VIEW).apply {
                data = url.toUri()
            }
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
    }
}