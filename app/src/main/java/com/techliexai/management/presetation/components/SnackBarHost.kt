package com.techliexai.management.presetation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.ui.theme.ColorError
import com.techliexai.management.ui.theme.ColorGreen
import com.techliexai.management.ui.theme.ColorWarning

@Composable
fun SnackBarHost(
    hostState: SnackbarHostState,
    type: MessageType
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SnackbarHost(
            hostState = hostState,
            snackbar = { data ->
                // Create a Card to give the Snackbar elevation and rounded corners
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 24.dp) // Padding around Snackbar
                        .padding(top = 24.dp)
                        .align(Alignment.TopCenter),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp), // Add elevation for shadow effect
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background) // Use background color from theme
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp), // Padding inside the card
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Icon depending on the MessageType
                        Icon(
                            imageVector = when(type) {
                                MessageType.SUCCESS -> Icons.Filled.CheckCircle
                                MessageType.ERROR -> Icons.Default.Error
                                MessageType.WARNING -> Icons.Default.Warning
                                MessageType.INFO -> Icons.Default.Info
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp), // Set consistent icon size
                            tint = when(type) {
                                MessageType.SUCCESS -> ColorGreen
                                MessageType.ERROR -> ColorError
                                MessageType.WARNING -> ColorWarning
                                MessageType.INFO -> Color.Black
                            }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Snackbar text message
                        Text(
                            text = data.visuals.message,
                            style = MaterialTheme.typography.bodyMedium,
//                            color = when(type) {
//                                MessageType.SUCCESS -> Color.Green
//                                MessageType.ERROR -> Color.Red
//                                MessageType.WARNING -> Color.Yellow
//                                MessageType.INFO -> Color.Black
//                            }
                        )
                    }
                }
            }
        )
    }
}