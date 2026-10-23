package com.techliexai.management.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

object ThemeManager {
    var currentThemeMode by mutableStateOf(ThemeMode.SYSTEM)
}

val LocalThemeMode = staticCompositionLocalOf { ThemeMode.SYSTEM }

private val DarkColorScheme = darkColorScheme(
    primary = ResolvaxPrimary,
    onPrimary = ResolvaxPureWhite,
    primaryContainer = ResolvaxDeepViolet,
    onPrimaryContainer = ResolvaxSoftLilac,
    secondary = ResolvaxViolet,
    onSecondary = ResolvaxPureWhite,
    secondaryContainer = ResolvaxDarkSurfaceVariant,
    onSecondaryContainer = ResolvaxSoftLilac,
    tertiary = ResolvaxSoftLilac,
    onTertiary = ResolvaxDeepPurple,
    background = ResolvaxDeepPurple,
    onBackground = ResolvaxOffWhite,
    surface = ResolvaxDarkSurface,
    onSurface = ResolvaxPureWhite,
    surfaceVariant = ResolvaxDarkSurfaceVariant,
    onSurfaceVariant = ResolvaxSoftLilac.copy(alpha = 0.8f),
    outline = ResolvaxSlateGray,
    outlineVariant = ResolvaxDeepViolet.copy(alpha = 0.5f),
    error = ResolvaxError,
    onError = ResolvaxPureWhite
)

private val LightColorScheme = lightColorScheme(
    primary = ResolvaxViolet,
    onPrimary = ResolvaxPureWhite,
    primaryContainer = ResolvaxLightSurfaceVariant,
    onPrimaryContainer = ResolvaxDeepViolet,
    secondary = ResolvaxPrimary,
    onSecondary = ResolvaxPureWhite,
    secondaryContainer = ResolvaxLightCard,
    onSecondaryContainer = ResolvaxDeepPurple,
    tertiary = ResolvaxDeepViolet,
    onTertiary = ResolvaxPureWhite,
    background = ResolvaxOffWhite,
    onBackground = ResolvaxNearBlack,
    surface = ResolvaxLightSurface,
    onSurface = ResolvaxNearBlack,
    surfaceVariant = ResolvaxLightSurfaceVariant,
    onSurfaceVariant = ResolvaxSlateGray,
    outline = ResolvaxSlateGray,
    outlineVariant = ResolvaxSoftLilac.copy(alpha = 0.4f),
    error = ResolvaxError,
    onError = ResolvaxPureWhite
)

@Composable
fun ManagementTheme(
    themeMode: ThemeMode = ThemeManager.currentThemeMode,
    darkTheme: Boolean = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    },
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
