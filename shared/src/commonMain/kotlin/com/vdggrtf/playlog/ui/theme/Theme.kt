package com.vdggrtf.playlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberpunkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    secondary = AiAccent,
    background = Background,
    surface = CardBackground,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun PlayLogTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberpunkColorScheme,
        typography = Typography, // Твои шрифты (оставляем)
        content = content
    )
}