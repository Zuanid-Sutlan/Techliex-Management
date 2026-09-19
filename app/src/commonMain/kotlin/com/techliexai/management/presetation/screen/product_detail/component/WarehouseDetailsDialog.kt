package com.techliexai.management.presetation.screen.product_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.techliexai.management.presetation.screen.product_detail.ProductDetailScreenAction
import com.techliexai.management.presetation.screen.product_detail.ProductDetailScreenState

@Composable
fun WarehouseDetailsDialog(
    state: ProductDetailScreenState,
    onAction: (ProductDetailScreenAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        // Main Container
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF1E1E1E), // Dark Gray/Black
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon and Title
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Warehouse Management",
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Input Fields ---
                OutlinedTextField(
                    value = if (state.warehousePrice == 0) "" else state.warehousePrice.toString(),
                    onValueChange = {
                        onAction(ProductDetailScreenAction.OnWarehousePriceChanged(it.toIntOrNull() ?: 0))
                    },
                    label = { Text("Warehouse Price", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Yellow,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
//                        textColor = Color.White,
                        cursorColor = Color.Yellow,
                        focusedLabelColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.warehouseNote,
                    onValueChange = { onAction(ProductDetailScreenAction.OnWarehouseNoteChanged(it)) },
                    label = { Text("Warehouse Note", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Yellow,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
//                        textColor = Color.White,
                        cursorColor = Color.Yellow,
                        focusedLabelColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Action Buttons ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = Color.Gray, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onAction(ProductDetailScreenAction.OnSaveWarehouseDetailsClicked)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Yellow),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp).padding(horizontal = 8.dp)
                    ) {
                        Text("SAVE DETAILS", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}