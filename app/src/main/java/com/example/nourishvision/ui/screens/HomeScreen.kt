// ui/screens/HomeScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary

@Composable
fun HomeScreen(onNavigateToBalance: () -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HeaderHome() }
            item { FoodBalanceCard(onClick = onNavigateToBalance) }
            item { SectionTitle("Riwayat Terbaru") }
            item { HistoryItem("Nasi + Ayam + Sayur", "86/100", "15/10/2024") }
            item { HistoryItem("Nasi + Telur", "62/100", "14/10/2024") }
        }
    }
}

@Composable
fun HeaderHome() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Hi, Zaki!", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("Hari ini apa yang kamu makan?", fontSize = 14.sp, color = Color.Gray)
        }
        IconButton(onClick = { /* Navigasi notifikasi */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "Notif")
        }
    }
}

@Composable
fun FoodBalanceCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = GreenLight),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Food Balance Score Hari Ini", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("78 / 100", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                Text("Cukup Seimbang", fontSize = 12.sp, color = GreenPrimary)
            }
            Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)) {
                Text("Detail")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun HistoryItem(name: String, score: String, date: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text(date, fontSize = 12.sp, color = Color.Gray)
            }
            Text(score, color = GreenPrimary, fontWeight = FontWeight.Bold)
        }
    }
}