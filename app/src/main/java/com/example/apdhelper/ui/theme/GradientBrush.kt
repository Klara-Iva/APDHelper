package com.example.apdhelper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun themedGradientBrush(): Brush {
    val isDark = isSystemInDarkTheme()
    return Brush.linearGradient(
        colors = if (isDark) {
            listOf(Surface, Secondary.copy(alpha = 0.3f))
        } else {
            listOf(Color(0xFFD8D3F2), LightSurface)
        }, start = Offset(0f, 0f), end = Offset(400f, 400f)
    )
}
