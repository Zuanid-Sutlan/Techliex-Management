package com.techliexai.management.presetation.screen.product_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.techliexai.management.presetation.screen.product_detail.component.WarehouseDetailsDialog
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.ui.theme.DesktopBreadcrumbHeader

@Composable
fun ProductDetailScreen(
    state: ProductDetailScreenState,
    isAdmin: Boolean,
    onAction: (ProductDetailScreenAction) -> Unit
) {
    var showWarehouseDialog by remember { mutableStateOf(false) }

    // Pure Desktop Product Detail View
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val product = state.product

        // Desktop Breadcrumb Header
        DesktopBreadcrumbHeader(
            parentCategory = "Product Hunts Directory",
            currentPage = product?.title ?: "Product Details",
            onBack = { EventManager.navigateBack() }
        )

        if (product != null) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isDesktop = maxWidth >= 800.dp

                if (isDesktop) {
                    // Desktop Split Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        if (product.productImage.isNotBlank()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                elevation = CardDefaults.cardElevation(2.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                            ) {
                                Box(modifier = Modifier.fillMaxSize().padding(20.dp).clip(RoundedCornerShape(16.dp))) {
                                    AsyncImage(
                                        model = product.productImage,
                                        contentDescription = product.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .fillMaxHeight(),
                            elevation = CardDefaults.cardElevation(2.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(28.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(product.title, color = MaterialTheme.colorScheme.primary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                                Text("Description", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(product.description, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, lineHeight = 22.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    Column {
                                        Text("Source Price", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("$${product.sourcePrice}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Column {
                                        Text("Warehouse Price", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("$${product.warehousePrice}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }

                                if (isAdmin) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { showWarehouseDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Warehouse, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Manage Warehouse Details", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (product.productImage.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AsyncImage(
                                    model = product.productImage,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(product.title, color = MaterialTheme.colorScheme.primary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(product.description, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Source Price: $${product.sourcePrice}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                                Text("Warehouse Price: $${product.warehousePrice}", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                                if (isAdmin) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { showWarehouseDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Warehouse, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Manage Warehouse", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showWarehouseDialog) {
                WarehouseDetailsDialog(
                    state = state,
                    onAction = onAction,
                    onDismiss = { showWarehouseDialog = false }
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text("Loading product details...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
