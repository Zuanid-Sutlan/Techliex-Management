package com.techliexai.management.domain.utils

import androidx.compose.ui.graphics.ImageBitmap

expect fun base64ToImageBitmap(base64String: String): ImageBitmap?
expect fun byteArrayToBase64(byteArray: ByteArray): String
