package com.techliexai.management.domain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

actual class ImagePicker {
    private var launcher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>? = null

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        val context = LocalContext.current
        launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val bytes = getBytesFromUri(context, it)
                if (bytes != null) {
                    onImagePicked(bytes)
                }
            }
        }
    }

    actual fun pickImage(onImagePicked: ((ByteArray) -> Unit)?) {
        launcher?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getBytesFromUri(context: Context, uri: Uri): ByteArray? {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }
}
