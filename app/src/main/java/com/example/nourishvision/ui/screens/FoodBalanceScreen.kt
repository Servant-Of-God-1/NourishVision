// ui/screens/FoodBalanceScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.ui.theme.GreenPrimary

@Composable
fun FoodBalanceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Balance Score") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Circular Score
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp).background(GreenPrimary, CircleShape)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(180.dp).background(Color.White, CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("85", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                        Text("/ 100", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Cukup Seimbang", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            // Rincian Nutrisi
            NutritionRow("Karbohidrat", "85%", "80%")
            NutritionRow("Protein", "80%", "70%")
            NutritionRow("Serat", "70%", "60%")

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Navigasi ke Rekomendasi */ },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Lihat Rekomendasi", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun NutritionRow(label: String, current: String, target: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text("${current} / ${target}", color = Color.Gray)
    }
    // Di sini Anda bisa menambahkan LinearProgressIndicator untuk bar hijau
    LinearProgressIndicator(
        progress = { current.toFloat() / 100f },
        color = GreenPrimary,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}