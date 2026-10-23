package com.techliexai.management.presetation.screen.orrder_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.ui.theme.DesktopBreadcrumbHeader

@Composable
fun OrderDetailScreen(
    state: OrderDetailScreenState,
    onAction: (OrderDetailScreenAction) -> Unit
) {
    // Pure Desktop Order Detail View
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val order = state.order

        // Desktop Breadcrumb Header
        DesktopBreadcrumbHeader(
            parentCategory = "Orders & Shipments",
            currentPage = if (order != null) "Order #${order.id}" else "Order Details",
            onBack = { EventManager.navigateBack() }
        )

        if (order != null) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isDesktop = maxWidth >= 800.dp

                if (isDesktop) {
                    // Desktop 2-Column Split Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
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
                                Text("Order #${order.id}", color = MaterialTheme.colorScheme.primary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                                DetailRow("Product Hunt Title", order.productHuntTitle)
                                DetailRow("Order Status", order.status)
                                DetailRow("Shipping Address", order.address)
                                DetailRow("Order Quantity", order.quantity.toString())
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Listing Price: $${order.listingPrice}",
                                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        if (order.paymentImage.isNotBlank()) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                elevation = CardDefaults.cardElevation(2.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text("Payment Screenshot", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        AsyncImage(
                                            model = order.paymentImage,
                                            contentDescription = "Payment Screenshot",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Fit
                                        )
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
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Order #${order.id}", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                Text("Product: ${order.productHuntTitle}", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Text("Status: ${order.status}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                                Text("Address: ${order.address}", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                Text("Quantity: ${order.quantity}", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Listing Price: $${order.listingPrice}", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (order.paymentImage.isNotBlank()) {
                            Text("Payment Screenshot", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AsyncImage(
                                    model = order.paymentImage,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text("Loading order details...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}
