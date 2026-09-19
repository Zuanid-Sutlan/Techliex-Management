package com.techliexai.management.domain.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun base64ToImageBitmap(base64String: String): ImageBitmap? {
    if (base64String.isEmpty()) return null
    return try {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        decodedImage?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

actual fun byteArrayToBase64(byteArray: ByteArray): String {
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
