package com.techliexai.management.domain.utils

import androidx.compose.runtime.Composable

expect class ImagePicker {
    @Composable
    fun registerPicker(onImagePicked: (ByteArray) -> Unit)
    fun pickImage(onImagePicked: ((ByteArray) -> Unit)? = null)
}
