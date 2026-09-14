package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = HospGoldAccent,
    onPrimary = HospNavyDark,
    primaryContainer = HospNavyLight,
    onPrimaryContainer = Color.White,
    secondary = HospGoldBright,
    onSecondary = Color.Black,
    tertiary = HospEmerald,
    background = HospNavyDarkBg,
    surface = HospNavyDarkSurface,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = HospNavyDarkCard,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = HospNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = HospNavyLight,
    onPrimaryContainer = Color.White,
    secondary = HospGoldAccent,
    onSecondary = Color.White,
    secondaryContainer = HospGoldLight,
    onSecondaryContainer = HospNavyPrimary,
    tertiary = HospEmerald,
    background = HospBackgroundLight,
    surface = HospSurfaceLight,
    onBackground = HospTextPrimary,
    onSurface = HospTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = HospTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
