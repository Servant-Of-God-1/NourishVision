package com.example.nourishvision.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.data.ProgressHelper
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary
import com.example.nourishvision.ui.viewmodel.FoodLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onBack: () -> Unit,
    foodLogViewModel: FoodLogViewModel
) {
    val foodLogs by foodLogViewModel.foodLogs.collectAsState()
    val localScores by foodLogViewModel.localScores.collectAsState()

    LaunchedEffect(Unit) {
        foodLogViewModel.getFoodLogs()
    }
    val weeklyData = remember(foodLogs, localScores) {
        ProgressHelper.getWeeklyProgress(foodLogs, localScores)
    }
    val weeklyAverage = remember(weeklyData) {
        ProgressHelper.getWeeklyAverage(weeklyData)
    }
    val weeklyTotal = remember(weeklyData) {
        ProgressHelper.getWeeklyTotalFood(weeklyData)
    }
    val trend = remember(weeklyData) {
        ProgressHelper.getTrend(weeklyData)
    }
    val todayScore = remember(foodLogs, localScores) {
        ProgressHelper.getTodayScore(foodLogs, localScores)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Progress Mingguan",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = GreenLight),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Rata-rata Skor Minggu Ini", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$todayScore / 100",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Scan", fontSize = 12.sp, color = Color.Gray)
                            Text("$weeklyTotal", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tren", fontSize = 12.sp, color = Color.Gray)
                            Text(trend, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Grafik 7 Hari Terakhir", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEach { day ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Nilai di atas batang
                        Text(
                            text = if (day.averageScore > 0) "${day.averageScore}" else "-",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (day.averageScore > 0) GreenPrimary else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(((day.averageScore.coerceAtLeast(0) / 100f) * 150).dp)
                                .background(
                                    color = when {
                                        day.averageScore >= 75 -> GreenPrimary
                                        day.averageScore >= 50 -> Color(0xFFFFA726)
                                        day.averageScore >= 25 -> Color(0xFFFB8C00)
                                        else -> Color(0xFFE0E0E0)
                                    },
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day.date,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFFFB8C00)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = when {
                            weeklyAverage == 0 -> "Belum ada data minggu ini. Mulai scan makanan Anda!"
                            weeklyAverage >= 75 -> "Pola makan Anda sangat baik! Pertahankan."
                            weeklyAverage >= 50 -> "Pola makan cukup baik. Tingkatkan konsumsi sayur."
                            else -> "Pola makan perlu diperbaiki. Tambahkan sayur & protein."
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF5D4037)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}