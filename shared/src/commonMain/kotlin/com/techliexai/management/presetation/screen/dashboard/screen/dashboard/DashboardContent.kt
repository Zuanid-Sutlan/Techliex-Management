package com.techliexai.management.presetation.screen.dashboard.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techliexai.management.presetation.screen.dashboard.DashboardAction
import com.techliexai.management.presetation.screen.dashboard.DashboardState
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems

@Composable
fun DashboardContent(padding: PaddingValues, state: DashboardState, onAction: (DashboardAction) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        val windowWidth = maxWidth
        val horizontalPadding = if (windowWidth >= 900.dp) 32.dp else 20.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Responsive Desktop Header Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Welcome back, ${state.name}!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Role: ${state.userRole} • Here is your Techliex enterprise overview today.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (windowWidth >= 650.dp) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "System Active",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            // Overview Metrics Section (Responsive Grid Columns)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Overview Metrics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                when {
                    windowWidth >= 1050.dp -> {
                        // 3 Columns Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            StatCard(
                                title = "Product Hunts",
                                value = state.products.toString(),
                                icon = Icons.Default.ShoppingBag,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Active Orders",
                                value = state.activeOrders.toString(),
                                icon = Icons.Default.LocalShipping,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Total Revenue Shared",
                                value = state.totalEarnings,
                                icon = Icons.Default.People,
                                modifier = Modifier.weight(1f),
                                isHighlight = true
                            )
                        }
                    }

                    windowWidth >= 680.dp -> {
                        // 2 Columns Row + Full Width Row
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                StatCard("Product Hunts", state.products.toString(), Icons.Default.ShoppingBag, Modifier.weight(1f))
                                StatCard("Active Orders", state.activeOrders.toString(), Icons.Default.LocalShipping, Modifier.weight(1f))
                            }
                            StatCard(
                                title = "Total Revenue Shared",
                                value = state.totalEarnings,
                                icon = Icons.Default.People,
                                modifier = Modifier.fillMaxWidth(),
                                isHighlight = true
                            )
                        }
                    }

                    else -> {
                        // Stacked 1 Column
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard("Product Hunts", state.products.toString(), Icons.Default.ShoppingBag, Modifier.fillMaxWidth())
                            StatCard("Active Orders", state.activeOrders.toString(), Icons.Default.LocalShipping, Modifier.fillMaxWidth())
                            StatCard("Total Revenue Shared", state.totalEarnings, Icons.Default.People, Modifier.fillMaxWidth(), isHighlight = true)
                        }
                    }
                }
            }

            // Quick Actions Section (Responsive Columns)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (windowWidth >= 680.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        ActionCard(
                            title = "Share Order Details",
                            subtitle = "Notify mentor to ship product",
                            icon = Icons.Default.LocalShipping,
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.ORDERS)) }
                        )
                        ActionCard(
                            title = "Hunt History",
                            subtitle = "Check status of previous hunts",
                            icon = Icons.Default.History,
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.PRODUCTS)) }
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActionCard(
                            title = "Share Order Details",
                            subtitle = "Notify mentor to ship product",
                            icon = Icons.Default.LocalShipping,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.ORDERS)) }
                        )
                        ActionCard(
                            title = "Hunt History",
                            subtitle = "Check status of previous hunts",
                            icon = Icons.Default.History,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.PRODUCTS)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isHighlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlight) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isHighlight) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isHighlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (isHighlight) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isHighlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
