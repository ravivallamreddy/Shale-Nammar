package com.example.shalenamma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    secondary = BrandPurple,
    tertiary = SuccessGreen,
    background = NavySlate,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFE2E8F0), // Lighter gray for better visibility in dark mode
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFF475569),
    surfaceVariant = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    secondary = BrandPurple,
    tertiary = SuccessGreen,
    background = CreamBg,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = NavySlate,
    onSurface = NavySlate,
    onSurfaceVariant = Color(0xFF64748B), // Darker gray for light mode
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFFF1F5F9)
)

@Composable
fun ShaleNammaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background,
            content = content
        )
    }
}
