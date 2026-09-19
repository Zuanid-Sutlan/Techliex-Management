package com.techliexai.management.presetation.screen.orrder_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.techliexai.management.domain.model.Order
import com.techliexai.management.presetation.components.enums.MessageType
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.domain.utils.base64ToImageBitmap

@Composable
fun OrderDetailScreen(
    state: OrderDetailScreenState,
    onAction: (OrderDetailScreenAction) -> Unit
) {
    val order = state.order ?: return
    var showUpdateDialog by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboard.current

    if (showUpdateDialog) {
        UpdateOrderAdminDialog(
            order = order,
            onDismiss = { showUpdateDialog = false },
            onUpdate = { trackId, company ->
                onAction(OrderDetailScreenAction.OnUpdateOrderDetailClicked(trackId, company))
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFF121212),
        bottomBar = {
            if (state.isAdmin) {
                Button(
                    onClick = { showUpdateDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(20.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("UPDATE TRACKING INFO", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { onAction(OrderDetailScreenAction.OnCompleteClicked) },
                    modifier = Modifier.fillMaxWidth().padding(20.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("ORDER COMPLETED", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            // --- Header with Back Button ---
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                val productBitmap = remember(order.productImage) { base64ToImageBitmap(order.productImage) }
                if (productBitmap != null) {
                    Image(productBitmap, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
                IconButton(
                    onClick = { onAction(OrderDetailScreenAction.OnNavigateBackClicked) },
                    modifier = Modifier.padding(16.dp).background(Color.Black.copy(0.4f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Yellow)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // --- Title & Status ---
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.weight(1f)) {
                        Text(order.productHuntTitle, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Ordered on ${order.date}", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text("$${order.listingPrice}", color = Color.Yellow, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                }

                Spacer(Modifier.height(24.dp))

                // --- Shipping Info Card ---
                InfoSection(title = "Shipping Address", icon = Icons.Default.LocationOn) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(order.address, color = Color.White, fontSize = 14.sp)
                        IconButton(
                            onClick = {
                                clipboardManager.nativeClipboard.text = order.address
                                EventManager.showMessage("Address copied to clipboard!", MessageType.SUCCESS)
                            }
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Yellow)
                        }
                    }
                }

                // --- Order Details Row ---
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailSmallCard("Quantity", order.quantity, Modifier.weight(1f))
                    DetailSmallCard("Variation", order.variationNote.ifBlank { "None" }, Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))

                // --- Admin Tracking Section ---
                if (order.trackId.isNotEmpty()) {
                    InfoSection(title = "Logistics", icon = Icons.Default.Send) {
                        Text("Company: ${order.company}", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Track ID: ${order.trackId}", color = Color.Yellow, fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // --- Payment Proof ---
                Text("Payment Proof", color = Color.Yellow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                val paymentBitmap = remember(order.paymentImage) { base64ToImageBitmap(order.paymentImage) }
                Card(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    if (paymentBitmap != null) {
                        Image(paymentBitmap, null, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateOrderAdminDialog(
    order: Order,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var trackingId by remember { mutableStateOf(order.trackId) }
    var companyName by remember { mutableStateOf(order.company) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1E1E),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Update Logistics", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Add shipping provider and tracking number", color = Color.Gray, fontSize = 12.sp)

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Courier Company (e.g. FedEx, TCS)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Yellow)
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = trackingId,
                    onValueChange = { trackingId = it },
                    label = { Text("Tracking ID") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Yellow)
                )

                Spacer(Modifier.height(32.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("CANCEL", color = Color.Gray) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onUpdate(trackingId, companyName); onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                    ) {
                        Text("SAVE INFO", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                color = Color.Yellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray.copy(0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun DetailSmallCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}