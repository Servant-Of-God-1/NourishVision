package com.example.nourishvision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.nourishvision.ui.theme.GreenPrimary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.data.FoodBalanceDatabase
import com.example.nourishvision.ui.components.CircularScoreIndicator
import com.example.nourishvision.ui.components.getScoreColor
import com.example.nourishvision.ui.components.getScoreLabel
import com.example.nourishvision.ui.theme.GreenLight
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import com.example.nourishvision.data.model.FoodBalanceData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodBalanceScreen(
    foodName: String,
    confidence: Double,
    userId: Int,
    onBack: () -> Unit,
    onSaveHistory: (FoodBalanceData) -> Unit,
    isSaved: Boolean = false,
    isLoading: Boolean = false
) {
    val foodData = remember(foodName) { FoodBalanceDatabase.getFoodData(foodName) }
    val scoreColor = getScoreColor(foodData.foodBalanceScore)
    val scoreLabel = getScoreLabel(foodData.foodBalanceScore)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Skor Gizi",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CircularScoreIndicator(
                score = foodData.foodBalanceScore,
                indicatorSize = 220.dp,
                strokeWidth = 18.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = foodData.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = foodData.description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            NutritionRow("Karbohidrat", foodData.carbohydrate, "g")
            NutritionRow("Protein", foodData.protein, "g")
            NutritionRow("Lemak", foodData.fat, "g")
            NutritionRow("Serat", foodData.fiber, "g")
            NutritionRow("Kalori", foodData.calories.toDouble(), "kcal")

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = GreenLight),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(scoreColor, RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Status Gizi: $scoreLabel",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = when {
                                    foodData.foodBalanceScore < 25 ->
                                        "Perlu perbaikan pola makan untuk gizi seimbang."
                                    foodData.foodBalanceScore < 50 ->
                                        "Cukup baik, tingkatkan konsumsi sayur & buah."
                                    foodData.foodBalanceScore < 75 ->
                                        "Baik! Pertahankan pola makan seimbang."
                                    else ->
                                        "Sangat baik! Pola makan Anda sudah ideal."
                                },
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onSaveHistory(foodData) },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Simpan ke Riwayat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionRow(label: String, value: Double, unit: String) {
    val maxValue = when (label) {
        "Karbohidrat" -> 100.0
        "Protein" -> 50.0
        "Lemak" -> 50.0
        "Serat" -> 20.0
        "Kalori" -> 800.0
        else -> 100.0
    }

    val progress = (value / maxValue).coerceIn(0.0, 1.0).toFloat()
    val barColor = when {
        progress < 0.25 -> Color(0xFFE53935)
        progress < 0.5 -> Color(0xFFFB8C00)
        progress < 0.75 -> Color(0xFFFDD835)
        else -> Color(0xFF43A047)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Medium, color = Color.Black)
            Text(
                text = if (unit == "kcal") "${value.toInt()} $unit"
                else "${value.toInt()} $unit",
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = barColor,
            trackColor = Color(0xFFEEEEEE),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}