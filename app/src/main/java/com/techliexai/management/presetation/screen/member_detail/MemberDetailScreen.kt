package com.techliexai.management.presetation.screen.member_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemberDetailScreen(
    state: MemberDetailScreeState,
    onAction: (MemberDetailAction) -> Unit
) {

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF121212))
        ) {
            // --- Custom Top Bar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onAction(MemberDetailAction.OnBackClicked) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Yellow
                    )
                }
                Text(
                    text = "Member Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(enabled = state.user.role != "Admin", onClick = { onAction(MemberDetailAction.OnDeleteMemberClicked) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.8f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // --- Profile Section ---
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Yellow, CircleShape)
                        .border(4.dp, Color(0xFF1E1E1E), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.user.name.take(1).uppercase(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = state.user.name,
//                    color = Color.White,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold
//                )

                // --- Status Badge ---
//                Surface(
//                    color = if (state.user.isActive) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(
//                        alpha = 0.1f
//                    ),
//                    shape = RoundedCornerShape(50.dp),
//                    border = BorderStroke(1.dp, if (state.user.isActive) Color.Green else Color.Red)
//                ) {
//                    Text(
//                        text = if (state.user.isActive) "ACTIVE ACCOUNT" else "INACTIVE",
//                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
//                        color = if (state.user.isActive) Color.Green else Color.Red,
//                        fontSize = 10.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- Information Cards ---
                InfoRow(
                    label = "Username",
                    value = "@${state.user.username}",
                    icon = Icons.Default.AlternateEmail
                )
                InfoRow(
                    label = "Assigned Role",
                    value = state.user.role,
                    icon = Icons.Default.Badge
                )
                InfoRow(
                    label = "User ID",
                    value = "#${state.user.id}",
                    icon = Icons.Default.Fingerprint
                )
                InfoRow(label = "Security", value = "••••••••", icon = Icons.Default.Lock)

                Spacer(modifier = Modifier.weight(1f))

                // --- Edit Action ---
                Button(
                    enabled = state.user.role != "Admin",
                    onClick = { onAction(MemberDetailAction.OnEditMemberClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "EDIT MEMBER PROFILE",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Yellow,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, color = Color.Gray, fontSize = 12.sp)
            Text(
                text = value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}