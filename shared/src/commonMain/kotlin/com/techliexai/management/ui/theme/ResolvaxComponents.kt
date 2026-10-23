package com.techliexai.management.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Prominent Techliex Application Brand Header
 */
@Composable
fun TechliexBrandHeader(
    modifier: Modifier = Modifier,
    logoSize: Dp = 42.dp,
    showTagline: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(logoSize)
                .clip(RoundedCornerShape(logoSize * 0.28f))
                .background(
                    Brush.linearGradient(
                        colors = listOf(ResolvaxPrimary, ResolvaxViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "T",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = (logoSize.value * 0.55f).sp
            )
        }
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)) {
                        append("Techliex")
                    }
                    withStyle(SpanStyle(color = ResolvaxPrimary, fontWeight = FontWeight.Bold)) {
                        append(" AI")
                    }
                },
                fontSize = (logoSize.value * 0.48f).sp,
                letterSpacing = (-0.5).sp
            )
            if (showTagline) {
                Text(
                    text = "MANAGEMENT SUITE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Enterprise Desktop Breadcrumb Header Bar for Detail Views
 */
@Composable
fun DesktopBreadcrumbHeader(
    parentCategory: String,
    currentPage: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (onBack != null) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.height(20.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                Text(
                    text = parentCategory,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "/",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = currentPage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = actions
            )
        }
    }
}

/**
 * Custom Resolvax "X" Icon matching the brand kit identity
 */
@Composable
fun ResolvaxLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(
                Brush.linearGradient(
                    colors = listOf(ResolvaxDeepPurple, ResolvaxDeepViolet)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(size * 0.18f)) {
            val w = this.size.width
            val h = this.size.height
            val strokeWidth = w * 0.22f

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(ResolvaxPrimary, ResolvaxViolet)
                ),
                start = Offset(w * 0.18f, h * 0.18f),
                end = Offset(w * 0.82f, h * 0.82f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(ResolvaxSoftLilac, ResolvaxPrimary)
                ),
                start = Offset(w * 0.18f, h * 0.82f),
                end = Offset(w * 0.82f, h * 0.18f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            drawCircle(
                color = ResolvaxSoftLilac.copy(alpha = 0.6f),
                radius = strokeWidth * 0.7f,
                center = Offset(w / 2f, h / 2f)
            )

            drawCircle(
                color = Color.White,
                radius = strokeWidth * 0.4f,
                center = Offset(w / 2f, h / 2f)
            )
        }
    }
}

/**
 * Secondary Developer Signature displaying Resolvax brand badge
 */
@Composable
fun DeveloperSignature(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ResolvaxLogoIcon(size = 26.dp)
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)) {
                            append("Developed by ")
                        }
                        withStyle(
                            SpanStyle(
                                color = ResolvaxPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        ) {
                            append("RESOLVAX")
                        }
                    }
                )
                Text(
                    text = "CONNECT • RESOLVE • GROW",
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
