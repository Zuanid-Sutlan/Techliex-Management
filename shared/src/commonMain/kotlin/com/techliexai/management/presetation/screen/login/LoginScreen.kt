package com.techliexai.management.presetation.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    state: LoginScreenState = LoginScreenState(),
    onAction: (LoginScreenAction) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Techliex Management",
            color = Color.Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Sign in to continue (admin / admin)",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginScreenAction.OnUsernameChanged(it)) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Yellow,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Yellow,
                cursorColor = Color.Yellow
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginScreenAction.OnPasswordChanged(it)) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { onAction(LoginScreenAction.OnPasswordVisibilityClicked) }) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password",
                        tint = Color.Yellow
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Yellow,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Yellow,
                cursorColor = Color.Yellow
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onAction(LoginScreenAction.OnLoginClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "LOGIN",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }

        TextButton(
            onClick = { onAction(LoginScreenAction.OnContactAdminClicked) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "Facing issues? Contact Admin",
                color = Color.Yellow.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable { onAction(LoginScreenAction.OnDeveloperInfoClicked) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Developed by Zunaid Sultan",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}
