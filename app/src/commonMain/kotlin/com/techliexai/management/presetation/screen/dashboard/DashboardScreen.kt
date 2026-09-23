package com.techliexai.management.presetation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.DrawerValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.presetation.screen.dashboard.screen.DrawerItems
import com.techliexai.management.presetation.screen.dashboard.screen.dashboard.DashboardContent
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DashboardDrawerContent(state = state, onAction = { onAction(it); scope.launch { drawerState.close() } })
        },
        scrimColor = Color.Black.copy(alpha = 0.7f)
    ) {
        Scaffold(
            containerColor = Color(0xFF121212),
            topBar = {
                DashboardTopBar(
                    userName = state.name,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onProfileClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.PROFILE)) }
                )
            },
            floatingActionButton = {
//                ExtendedFloatingActionButton(
//                    text = { Text("New Hunt", color = Color.Black) },
//                    onClick = { onAction(DashboardAction.OnNewHuntClicked) },
//                    icon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black) },
//                    backgroundColor = Color.Yellow,
//                    shape = RoundedCornerShape(16.dp)
//                )
            }
        ) { padding ->
//            when (state.selectedItem) {
//                DrawerItems.DASHBOARD -> {
                    DashboardContent(padding, state, onAction)
//                }

//                DrawerItems.MEMBERS -> {
//                    MemberScreen(
//                        paddingValues = padding,
//                        state = state.membersState,
//                        onAction = onAction
//                    )
//                }
//                else -> {}
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(userName: String, onMenuClick: () -> Unit, onProfileClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(Color(0xFF121212)),
//        elevation = 0.dp,
        title = {
            Column {
                Text(
                    "Hello,",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    userName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Yellow)
            }
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color.Yellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}

@Composable
fun DashboardDrawerContent(state: DashboardState, onAction: (DashboardAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f) // Occupies 75% of screen width
            .background(Color(0xFF1E1E1E)) // Slightly lighter than the main background
            .padding(top = 24.dp)
            .padding(24.dp)
    ) {
        // --- Profile Header ---
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Yellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.name.take(1).uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Role: ${state.userRole}",
            color = Color.Yellow,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(40.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(20.dp))

        // --- Navigation Items ---
        DrawerItem(
            icon = Icons.Default.Home,
            label = DrawerItems.DASHBOARD,
            isSelected = state.selectedItem == DrawerItems.DASHBOARD,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.DASHBOARD)) }
        )
        if (state.userRole == "Admin") {
            DrawerItem(
                icon = Icons.Default.Person,
                label = DrawerItems.MEMBERS,
                isSelected = state.selectedItem == DrawerItems.MEMBERS,
                onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.MEMBERS)) }
            )
        }
        DrawerItem(
            icon = Icons.Default.List,
            label = DrawerItems.PRODUCTS,
            isSelected = state.selectedItem == DrawerItems.PRODUCTS,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.PRODUCTS)) }
        )

        DrawerItem(
            icon = Icons.Default.ShoppingCart,
            label = DrawerItems.ORDERS,
            isSelected = state.selectedItem == DrawerItems.ORDERS,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.ORDERS)) }
        )

        DrawerItem(
            icon = Icons.Default.Person,
            label = DrawerItems.MENTOR_CONNECT,
            isSelected = state.selectedItem == DrawerItems.MENTOR_CONNECT,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.MENTOR_CONNECT)) }
        )
        DrawerItem(
            icon = Icons.Default.CheckCircle,
            label = DrawerItems.EARNINGS,
            isSelected = state.selectedItem == DrawerItems.EARNINGS,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.EARNINGS)) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- Bottom Actions ---
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(12.dp))

        DrawerItem(
            icon = Icons.Default.Settings,
            label = DrawerItems.SETTINGS,
            isSelected = state.selectedItem == DrawerItems.SETTINGS,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.SETTINGS)) },
            tint = Color.Gray
        )
        DrawerItem(
            icon = Icons.Default.ExitToApp,
            label = DrawerItems.LOGOUT,
            onClick = { onAction(DashboardAction.OnNavigateContentClicked(DrawerItems.LOGOUT)) },
            tint = Color.Red.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: DrawerItems,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.Yellow.copy(alpha = 0.1f) else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.Yellow else tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label.label,
            color = if (isSelected) Color.Yellow else tint,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}