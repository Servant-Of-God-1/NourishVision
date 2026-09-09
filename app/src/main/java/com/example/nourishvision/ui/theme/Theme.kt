// ui/theme/Theme.kt
package com.example.nourishvision.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onPrimary = BackgroundWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(32.dp)
)

@Composable
fun NourishVisionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(), // Anda bisa menyesuaikan font (Poppins/Montserrat) di sini
        shapes = AppShapes,
        content = content
    )
}