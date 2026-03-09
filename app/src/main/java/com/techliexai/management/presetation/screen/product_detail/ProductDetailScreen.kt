package com.techliexai.management.presetation.screen.product_detail

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Uri
import com.techliexai.management.presetation.screen.product_detail.component.WarehouseDetailsDialog
import com.techliexai.management.presetation.utils.toBitmap
import androidx.core.net.toUri
import com.techliexai.management.domain.model.ProductHunt

@Composable
fun ProductDetailScreen(
    state: ProductDetailScreenState,
    isAdmin: Boolean, // Pass this from your User Session/ViewModel
    onAction: (ProductDetailScreenAction) -> Unit
) {
    var showWarehouseDialog by remember { mutableStateOf(false) }
    val product = state.product ?: ProductHunt()
    val bitmap = remember(product.productImage) { product.productImage.toBitmap() }



    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF121212))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // --- Image Header with Back Button ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = product.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Top Action Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { onAction(ProductDetailScreenAction.OnNavigateBackClicked) },
                            modifier = Modifier.background(Color.Black.copy(0.4f), CircleShape)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.Yellow
                            )
                        }

                        if (isAdmin) {
                            IconButton(
                                onClick = { onAction(ProductDetailScreenAction.OnDeleteClicked) },
                                modifier = Modifier.background(Color.Black.copy(0.4f), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }

                // --- Product Content ---
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = product.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Added by ${product.addedBy}",
                        color = Color.Yellow,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = product.description, color = Color.Gray, fontSize = 15.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Comparison Row ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PriceInfoCard(
                            "Source Price",
                            "$${product.sourcePrice}",
                            Modifier.weight(1f)
                        )
                        PriceInfoCard(
                            "Ref Price",
                            "$${product.referencePrice}",
                            Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    PriceInfoCard(
                        "Warehouse Price",
                        "$${product.warehousePrice}",
                        Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Warehouse Message: ${product.warehouseNote}",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Links Section ---
                    LinkButton(label = "Source Website", url = product.sourceLink)
                    LinkButton(label = "Reference Website", url = product.referenceLink)

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Admin Exclusive Section ---
                    if (isAdmin) {
                        Button(
                            onClick = { showWarehouseDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF1E1E1E)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Yellow)
                        ) {
                            Icon(
                                Icons.Default.Warehouse,
                                contentDescription = null,
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SET WAREHOUSE DETAILS", color = Color.Yellow)
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom button
                }
            }

            // --- Bottom Action Button (Visible to Everyone) ---
            Button(
                onClick = { onAction(ProductDetailScreenAction.OnEditClicked) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(Color.Yellow),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("UPDATE PRODUCT DATA", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            // --- Warehouse Dialog (Admin Only) ---
            if (showWarehouseDialog) {
                WarehouseDetailsDialog(
                    state = state,
                    onAction = onAction,
                    onDismiss = { showWarehouseDialog = false }
                )
            }
        }
    }
}

@Composable
fun PriceInfoCard(label: String, price: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(price, color = Color.Yellow, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun LinkButton(
    label: String,
    url: String
) {
    val context = LocalContext.current

    // Only show the button if the URL isn't empty
    if (url.isNotBlank()) {
        OutlinedButton(
            onClick = {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Handle cases where the URL might be malformed or no browser is installed
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}