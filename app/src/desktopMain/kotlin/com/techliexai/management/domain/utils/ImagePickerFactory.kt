package com.techliexai.management.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}
