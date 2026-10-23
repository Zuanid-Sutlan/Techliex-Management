package com.techliexai.management.presetation.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techliexai.management.ui.theme.DeveloperSignature
import com.techliexai.management.ui.theme.TechliexBrandHeader

@Composable
fun LoginScreen(
    state: LoginScreenState = LoginScreenState(),
    onAction: (LoginScreenAction) -> Unit = {},
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val windowWidth = maxWidth
        val isWideView = windowWidth >= 900.dp

        if (isWideView) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Pane: Techliex Management Platform Information
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 48.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    TechliexBrandHeader(logoSize = 52.dp, showTagline = true)

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Enterprise Operations & Workflow System",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Unified Management Platform",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Streamline team members, product hunts, customer orders, and warehouse logistics efficiently across desktop workstations.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Techliex Management v1.0 • Desktop Suite",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    DeveloperSignature(
                        onClick = { onAction(LoginScreenAction.OnDeveloperInfoClicked) }
                    )
                }

                // Right Pane: Desktop Login Form Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 48.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginFormContent(state, onAction)
                }
            }
        } else {
            // Narrower Window Layout: Centered Login Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TechliexBrandHeader(logoSize = 48.dp, showTagline = true)
                        Spacer(modifier = Modifier.height(24.dp))
                        LoginFormContent(state, onAction)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginFormContent(
    state: LoginScreenState,
    onAction: (LoginScreenAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Techliex",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Sign in to your enterprise account (admin / admin)",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginScreenAction.OnUsernameChanged(it)) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onAction(LoginScreenAction.OnLoginClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "SIGN IN",
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
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        Spacer(modifier = Modifier.height(20.dp))

        DeveloperSignature(
            modifier = Modifier.clickable { onAction(LoginScreenAction.OnDeveloperInfoClicked) },
            onClick = { onAction(LoginScreenAction.OnDeveloperInfoClicked) }
        )
    }
}
