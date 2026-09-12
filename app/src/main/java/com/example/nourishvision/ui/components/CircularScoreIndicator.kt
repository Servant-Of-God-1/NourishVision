// ui/components/CircularScoreIndicator.kt
package com.example.nourishvision.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Mendapatkan warna berdasarkan skor gizi (0-100).
 */
fun getScoreColor(score: Int): Color {
    return when {
        score < 25 -> Color(0xFFE53935)   // Merah - Kurang
        score < 50 -> Color(0xFFFB8C00)   // Orange - Cukup
        score < 75 -> Color(0xFFFDD835)   // Kuning - Baik
        else -> Color(0xFF43A047)          // Hijau - Sangat Baik
    }
}

/**
 * Mendapatkan label kategori berdasarkan skor.
 */
fun getScoreLabel(score: Int): String {
    return when {
        score < 25 -> "Kurang"
        score < 50 -> "Cukup"
        score < 75 -> "Baik"
        else -> "Sangat Baik"
    }
}

/**
 * Circular score indicator dengan warna dinamis dan animasi.
 *
 * @param score Nilai skor 0-100
 * @param modifier Modifier Compose
 * @param indicatorSize Ukuran lingkaran (default 200.dp)
 * @param strokeWidth Ketebalan garis lingkaran (default 16.dp)
 */
@Composable
fun CircularScoreIndicator(
    score: Int,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 200.dp,
    strokeWidth: Dp = 16.dp
) {
    val scoreColor = getScoreColor(score)
    val scoreLabel = getScoreLabel(score)
    val trackColor = Color(0xFFEEEEEE)  // Abu-abu untuk background

    // ✅ Animasi progress dari 0 ke skor
    val animatedProgress by animateFloatAsState(
        targetValue = (score / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200),
        label = "score_progress"
    )

    Box(
        modifier = modifier.size(indicatorSize),
        contentAlignment = Alignment.Center
    ) {
        // Canvas untuk menggambar lingkaran progress
        Canvas(modifier = Modifier.size(indicatorSize)) {
            // ✅ Gunakan `this.size` untuk ukuran Canvas, bukan parameter `indicatorSize`
            val canvasSize = this.size
            val strokeWidthPx = strokeWidth.toPx()
            val arcSize = Size(
                width = canvasSize.width - strokeWidthPx,
                height = canvasSize.height - strokeWidthPx
            )
            val topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)

            // 1. Background circle (abu-abu)
            drawArc(
                color = trackColor,
                startAngle = -90f,        // Mulai dari atas (jam 12)
                sweepAngle = 360f,        // Lingkaran penuh
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            val sweepAngle = animatedProgress * 360f
            if (sweepAngle > 0f) {
                drawArc(
                    color = scoreColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = "/ 100",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = scoreLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = scoreColor
            )
        }
    }
}