package com.techliexai.management.presetation.screen.product_add

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AddAPhoto
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.screen.product_add.component.MemberSelectionDialog
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.presetation.utils.toBase64

@Composable
fun AddProductScreen(
    state: AddProductScreenState,
    onAction: (AddProductScreenAction) -> Unit
) {
    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    var isGranted by remember { mutableStateOf(false) }


    // Image Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                onAction(AddProductScreenAction.OnProductImageChanged(bitmap.toBase64()))
            }
        }
    )

    // permission launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, handle photo access
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // Permission denied
            EventManager.showMessage("Permission Denied", MessageType.ERROR)
        }
    }

    // Initial check
    LaunchedEffect(Unit) {
        isGranted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
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
                        if (isGranted) {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        } else {
                            launcher.launch(permission)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (state.productImage.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Upload Product Image", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    // Decode Base64 to show preview
                    val imageBytes = Base64.decode(state.productImage, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Image(
                        bitmap = decodedImage.asImageBitmap(),
                        contentDescription = "Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
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