package com.techliexai.management.presetation.screen.product_add

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import com.techliexai.management.domain.utils.base64ToImageBitmap
import com.techliexai.management.domain.utils.byteArrayToBase64
import com.techliexai.management.domain.utils.rememberImagePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.screen.product_add.component.MemberSelectionDialog
import com.techliexai.management.presetation.utils.EventManager

@Composable
fun AddProductScreen(
    state: AddProductScreenState,
    onAction: (AddProductScreenAction) -> Unit
) {
    // Image Picker Launcher
    val imagePicker = rememberImagePicker()
    imagePicker.registerPicker { bytes ->
        val base64 = byteArrayToBase64(bytes)
        onAction(AddProductScreenAction.OnProductImageChanged(base64))
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .background(Color(0xFF121212))
                .verticalScroll(rememberScrollState())
        ) {
            // --- Custom App Bar ---
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onAction(AddProductScreenAction.OnNavigateBackClicked) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.Yellow
                    )
                }
                Text(
                    "Add New Product",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Image Picker Section ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1E1E1E))
                    .clickable {
                        imagePicker.pickImage()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (state.productImage.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Upload Product Image", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    // Decode Base64 to show preview
                    val decodedImage = base64ToImageBitmap(state.productImage)
                    if (decodedImage != null) {
                        Image(
                            bitmap = decodedImage,
                            contentDescription = "Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Form Fields ---
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ProductTextField(
                    value = state.title,
                    label = "Product Title",
                    onValueChange = { onAction(AddProductScreenAction.OnTitleChanged(it)) }
                )

                ProductTextField(
                    modifier = Modifier.height(100.dp),
                    value = state.note,
                    label = "Notes / Description",
                    isSingleLine = false,
                    onValueChange = { onAction(AddProductScreenAction.OnNoteChanged(it)) }
                )

                HorizontalDivider(
                    color = Color.Gray.copy(0.2f),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // --- Source Details ---
                Text(
                    "Source Info (Where you found it)",
                    color = Color.Yellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                ProductTextField(
                    value = state.sourceLink,
                    label = "Source Link",
                    onValueChange = { onAction(AddProductScreenAction.OnSourceLinkChanged(it)) }
                )
                ProductTextField(
                    value = state.sourcePrice.toString(),
                    label = "Source Price",
                    onValueChange = { v ->
                        onAction(
                            AddProductScreenAction.OnSourcePriceChanged(
                                v.toIntOrNull() ?: 0
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Reference Details ---
                Text(
                    "Reference Info (Market Comparison)",
                    color = Color.Yellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                ProductTextField(
                    value = state.referenceLink,
                    label = "Reference Link",
                    onValueChange = { onAction(AddProductScreenAction.OnReferenceLinkChanged(it)) }
                )
                ProductTextField(
                    value = state.referencePrice.toString(),
                    label = "Reference Price",
                    onValueChange = { v ->
                        onAction(
                            AddProductScreenAction.OnReferencePriceChanged(
                                v.toIntOrNull() ?: 0
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Share With ---
                val isExpanded = remember { mutableStateOf(false) }
                Text(
                    "Share With (Other Members)",
                    color = Color.Yellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    ProductTextField(
                        value = state.shareWith.joinToString(", ") { it.name },
                        label = "Select Members",
                        onValueChange = { }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                onClick = {
                                    isExpanded.value = !isExpanded.value
                                },
                                indication = null,
                                interactionSource = null
                            )
                    )
                }

                if (isExpanded.value) {
                    MemberSelectionDialog(
                        members = state.members,
                        onDismiss = { isExpanded.value = false },
                        onSaveMemberSelection = { users ->
                            onAction(AddProductScreenAction.OnShareWithChanged(users))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // --- Save Button ---
                Button(
                    onClick = { onAction(AddProductScreenAction.OnSaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(Color.Yellow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("SAVE PRODUCT", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProductTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    isSingleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = isSingleLine,
        maxLines = if (isSingleLine) 1 else 5,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Yellow,
            unfocusedBorderColor = Color.Gray.copy(0.4f),
//            textColor = Color.White,
            focusedLabelColor = Color.Yellow,
            cursorColor = Color.Yellow
        ),
        shape = RoundedCornerShape(12.dp)
    )
}