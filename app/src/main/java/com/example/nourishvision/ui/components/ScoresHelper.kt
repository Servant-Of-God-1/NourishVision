// ui/components/ScoreHelper.kt
package com.example.nourishvision.ui.components

import androidx.compose.ui.graphics.Color
import com.example.nourishvision.ui.theme.GreenPrimary


fun getScoreStatusLabel(score: Int): String {
    return when {
        score == 0 -> "Belum ada data"
        score < 25 -> "Kurang Seimbang"
        score < 50 -> "Cukup Seimbang"
        score < 75 -> "Seimbang"
        else -> "Sangat Seimbang"
    }
}

fun getScoreStatusColor(score: Int): Color {
    return when {
        score == 0 -> Color.Gray
        score < 25 -> Color(0xFFE53935)
        score < 50 -> Color(0xFFFB8C00)
        score < 75 -> Color(0xFF43A047)
        else -> GreenPrimary
    }
}

fun getScoreStatusDescription(score: Int): String {
    return when {
        score == 0 -> "Scan makanan untuk melihat skor gizi Anda."
        score < 25 -> "Perlu perbaikan pola makan untuk gizi seimbang."
        score < 50 -> "Cukup baik, tingkatkan konsumsi sayur & buah."
        score < 75 -> "Baik! Pertahankan pola makan seimbang."
        else -> "Sangat baik! Pola makan Anda sudah ideal."
    }
}