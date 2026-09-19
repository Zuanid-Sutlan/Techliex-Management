package com.techliexai.management.domain.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.util.Base64

actual fun base64ToImageBitmap(base64String: String): ImageBitmap? {
    if (base64String.isEmpty()) return null
    return try {
        val imageBytes = Base64.getDecoder().decode(base64String)
        Image.makeFromEncoded(imageBytes).toComposeImageBitmap()
    } catch (e: Exception) {
        null
    }
}

actual fun byteArrayToBase64(byteArray: ByteArray): String {
    return Base64.getEncoder().encodeToString(byteArray)
}
