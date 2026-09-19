package com.techliexai.management.presetation.screen.member_add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.presetation.components.RoleInputField

@Composable
fun CreateNewRole(
    state: CreateUserScreenState,
    onAction: (CreateUserAction) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize())  {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(it)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onAction(CreateUserAction.OnBackClicked) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Yellow)
                }
                Text(
                    text = "Create New Account",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Role Selection (Chip Style) ---
            Text(
                text = "Select User Role",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Student", "Mentor").forEach { role ->
                    val isSelected = state.role == role
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onAction(CreateUserAction.OnRoleSelected(role)) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color.Yellow else Color(0xFF1E1E1E),
                        border = BorderStroke(1.dp, if (isSelected) Color.Yellow else Color.Gray.copy(0.3f))
                    ) {
                        Text(
                            text = role,
                            modifier = Modifier.padding(vertical = 12.dp),
                            textAlign = TextAlign.Center,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Input Fields ---
            RoleInputField(
                value = state.name,
                label = "Full Name",
                icon = Icons.Default.Person,
                capitalization = true,
                onValueChange = { onAction(CreateUserAction.OnNameChanged(it)) }
            )

            RoleInputField(
                value = state.username,
                label = "Username / Email",
                icon = Icons.Default.Email,
                onValueChange = { onAction(CreateUserAction.OnUsernameChanged(it)) }
            )

            RoleInputField(
                value = state.password,
                label = "Temporary Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                onValueChange = { onAction(CreateUserAction.OnPasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Save Button ---
            Button(
                onClick = { onAction(CreateUserAction.OnSaveUser) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CREATE USER",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}