package com.techliexai.management.presetation.utils

expect fun openWhatsAppChat(phoneNumber: String)
expect fun openUrl(url: String)
fun isInternetAvailable(): Boolean = true

fun String.toBase64(): String = this
fun String.toBitmap(): Any? = null
fun String.toImageBytes(): ByteArray? = null
