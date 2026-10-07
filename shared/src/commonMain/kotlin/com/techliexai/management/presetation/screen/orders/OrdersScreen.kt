package com.techliexai.management.presetation.screen.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.domain.model.Order
import com.techliexai.management.presetation.navigation.Screen
import com.techliexai.management.presetation.utils.EventManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    state: OrderScreenState,
    onAction: (OrderScreenAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Scaffold(
            containerColor = Color(0xFF121212),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(Color(0xFF121212)),
                    title = { Text("Orders & Shipments", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { EventManager.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Yellow)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (state.orders.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No orders found", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.orders) { order ->
                            OrderItem(order = order, onClick = {
                                EventManager.navigateTo(Screen.OrderDetailScreen(order.id))
                            })
                        }
                    }
                }
            }
        }

        Button(
            onClick = { EventManager.navigateTo(Screen.AddOrderScreen) },
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
                contentDescription = "Add Order",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun OrderItem(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order #${order.id}", color = Color.Yellow, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Product: ${order.productHuntTitle}", color = Color.White)
            Text("Status: ${order.status}", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
