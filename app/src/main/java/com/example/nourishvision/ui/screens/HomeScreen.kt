// ui/screens/HomeScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.R
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary

data class HistoryItem(
    val id: Int,
    val foodName: String,
    val date: String,
    val score: Int,
    val imageRes: Int
)

@Composable
fun HomeScreen(
    onNavigateToBalance: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val historyList = remember {
        listOf(
            HistoryItem(1, "Nasi + Ayam + Sayur", "15/10/2024", 86, R.drawable.ic_launcher_background),
            HistoryItem(2, "Nasi + Telur", "15/10/2024", 62, R.drawable.ic_launcher_background),
            HistoryItem(3, "Bakso + Mie", "14/10/2024", 73, R.drawable.ic_launcher_background),
            HistoryItem(4, "Roti + Kopi", "14/10/2024", 70, R.drawable.ic_launcher_background),
            HistoryItem(5, "Nasi Goreng", "13/10/2024", 90, R.drawable.ic_launcher_background)
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderHome()
        }
        item {
            FoodBalanceCard(onClick = onNavigateToBalance)
        }
        item {
            SectionTitleWithAction(
                title = "Riwayat Terbaru",
                onSeeAll = onNavigateToHistory
            )
        }
        items(historyList) { item ->
            HistoryItemCard(
                item = item,
                onClick = onNavigateToHistory
            )
        }
    }
}

@Composable
fun HeaderHome() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(50)
            ) {
                Text("Detail")
            }
        }
    }
}

@Composable
fun SectionTitleWithAction(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = onSeeAll) {
            Text("Lihat Semua", color = GreenPrimary, fontSize = 12.sp)
        }
    }
}

// 3. Kartu Riwayat yang Lebih Lengkap
@Composable
fun HistoryItemCard(item: HistoryItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Makanan
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.foodName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.foodName, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(item.date, fontSize = 12.sp, color = Color.Gray)
            }

            Text(item.score.toString(), color = GreenPrimary, fontWeight = FontWeight.Bold)

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}