package com.techliexai.management

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Management Desktop"
    ) {
        App() // Assuming there is a shared App() composable
    }
}
