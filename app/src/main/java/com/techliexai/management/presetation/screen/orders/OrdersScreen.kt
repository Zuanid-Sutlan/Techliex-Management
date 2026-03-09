package com.techliexai.management.presetation.screen.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.domain.model.Order
import com.techliexai.management.domain.model.ProductHunt
import com.techliexai.management.presetation.screen.products.HuntProductScreenAction
import com.techliexai.management.presetation.utils.toBitmap

@Composable
fun OrdersScreen(
    state: OrderScreenState,
    onAction: (OrderScreenAction) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize())  {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF121212))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // --- Header ---
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onAction(OrderScreenAction.OnNavigateBackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Yellow)
                    }
                    Text(
                        text = "Order Management",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // --- Search Bar ---
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onAction(OrderScreenAction.OnSearchQueryChanged(it)) },
                    placeholder = { Text("Search by Product or Tracking ID", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Yellow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Yellow,
                        unfocusedBorderColor = Color.Gray.copy(0.3f),
//                        textColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- ORDERS List ---
                val filteredOrders = state.orders.filter {
                    it.productHuntTitle.contains(state.searchQuery, ignoreCase = true) ||
                            it.trackId.contains(state.searchQuery, ignoreCase = true)
                }

                if (filteredOrders.isEmpty()) {
                    EmptyOrderState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders) { order ->
                            OrderItemCard(
                                order = order,
                                onClick = { onAction(OrderScreenAction.OnOrderClicked(order)) }
                            )
                        }
                    }
                }
            }

            // --- Manual Floating Action Button ---
            Button(
                onClick = { onAction(OrderScreenAction.OnAddOrderClicked) },
                modifier = Modifier
                    .padding(bottom = 36.dp, end = 24.dp)
                    .align(Alignment.BottomEnd),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add order",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(order: Order, onClick: () -> Unit) {
    // 1. Define status properties (You can move this to a helper function later)
    val (statusLabel, statusColor, statusIcon) = when (order.status) {
        "Active" -> Triple("ACTIVE", Color.Yellow, Icons.Default.Pending)
        "Shipped" if order.company.isNotEmpty() -> Triple("SHIPPED", Color.Cyan, Icons.Default.LocalShipping)
        // Assuming you might add a 'isCompleted' boolean later, for now let's use a logic placeholder
        // or a specific flag if your Order model supports it.
        "Completed" -> Triple("COMPLETED", Color.Green, Icons.Default.CheckCircle)
        else -> Triple("UNKNOWN", Color.Gray, Icons.Default.ImageNotSupported)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Product Image ---
            val bitmap = remember(order.productImage) { order.productImage.toBitmap() }
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.Black
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Inventory, null, tint = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- Order Info ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.productHuntTitle,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // --- Status Badge (NEW) ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Text(
                    text = "By: ${order.addedBy} • ${order.date}",
                    color = Color.Gray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Track ID Badge
                if (order.trackId.isNotEmpty()) {
                    Text(
                        text = "TRK: ${order.trackId}",
                        color = Color.Yellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.Yellow.copy(0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // --- Price Section ---
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${order.listingPrice}",
                    color = Color.Yellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Qty: ${order.quantity}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EmptyOrderState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp), // Adjust for FAB space
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Large Faded Icon ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Yellow.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = Color.Yellow.copy(alpha = 0.2f),
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Message ---
        Text(
            text = "No ORDERS Found",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "When you or your students add orders,\nthey will appear here.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Optional: Small hint arrow pointing to FAB ---
        Text(
            text = "Tap + to create one",
            color = Color.Yellow.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
        )
    }
}