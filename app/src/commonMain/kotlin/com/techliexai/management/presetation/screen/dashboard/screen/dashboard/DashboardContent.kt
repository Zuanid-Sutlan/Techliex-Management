package com.techliexai.management.presetation.screen.dashboard.screen.dashboard

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.presetation.screen.dashboard.DashboardAction
import com.techliexai.management.presetation.screen.dashboard.DashboardState
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems

@Composable
fun DashboardContent(padding: PaddingValues, state: DashboardState, onAction: (DashboardAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Stats Overview Cards ---
        Text("Overview", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Product Hunts", state.products.toString(), Modifier.weight(1f))
            StatCard("Active Orders", state.activeOrders.toString(), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        StatCard(
            title = "Total Revenue Shared",
            value = state.totalEarnings,
            modifier = Modifier.fillMaxWidth(),
            isHighlight = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Quick Actions Section ---
        Text("Quick Actions", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        ActionRow(
            title = "Share Order Details",
            subtitle = "Notify mentor to ship product",
            icon = Icons.Default.ShoppingCart,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.ORDERS)) }
        )

        ActionRow(
            title = "Hunt History",
            subtitle = "Check status of previous hunts",
            icon = Icons.Default.List,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.PRODUCTS)) }
        )
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier, isHighlight: Boolean = false) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(if (isHighlight) Color.Yellow else Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, color = if (isHighlight) Color.Black else Color.Gray, fontSize = 12.sp)
            Text(value, color = if (isHighlight) Color.Black else Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun ActionRow(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(Color.Yellow.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
