package com.techliexai.management.domain.utils

import androidx.compose.runtime.Composable
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

actual class ImagePicker {
    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        // Desktop can just use a normal function call, 
        // no need to register a launcher like Android
    }

    actual fun pickImage(onImagePicked: ((ByteArray) -> Unit)?) {
        val fileDialog = FileDialog(Frame(), "Select Image", FileDialog.LOAD)
        fileDialog.isVisible = true
        val file = fileDialog.files.firstOrNull()
        if (file != null) {
            val bytes = file.readBytes()
            onImagePicked?.invoke(bytes)
        }
    }
}
