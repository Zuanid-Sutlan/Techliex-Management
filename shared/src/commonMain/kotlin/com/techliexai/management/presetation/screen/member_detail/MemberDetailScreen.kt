package com.techliexai.management.presetation.screen.member_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techliexai.management.presetation.utils.EventManager
import com.techliexai.management.ui.theme.DesktopBreadcrumbHeader
import com.techliexai.management.ui.theme.ResolvaxSuccess

@Composable
fun MemberDetailScreen(
    state: MemberDetailScreeState,
    onAction: (MemberDetailAction) -> Unit
) {
    // Pure Desktop Member Detail View
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Desktop Breadcrumb Header
        DesktopBreadcrumbHeader(
            parentCategory = "Directory & Members",
            currentPage = state.user?.name ?: "Member Profile",
            onBack = { EventManager.navigateBack() }
        )

        val user = state.user
        if (user != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.7f),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                    DetailField("Username", "@${user.username}")
                    DetailField("User Role", user.role)
                    DetailField("Account Status", if (user.isActive) "Active" else "Inactive", isStatus = true, isActive = user.isActive)
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading member details...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String, isStatus: Boolean = false, isActive: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isStatus) (if (isActive) ResolvaxSuccess else MaterialTheme.colorScheme.error) else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
