// ui/screens/HomeScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.automirrored.filled.TrendingUp

@Composable
fun HomeScreen(
    onNavigateToBalance: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { HeaderHome() }
            item { FoodBalanceCard(onClick = onNavigateToBalance) }
            item {
                SectionTitleWithAction(
                    title = "Riwayat Terbaru",
                    onSeeAll = onNavigateToHistory
                )
            }

            // Ubah HistoryItem agar menampilkan gambar bulat
            item {
                HistoryItem(
                    imageRes = R.drawable.ic_launcher_background, // Ganti dengan gambar makanan Anda
                    name = "Nasi + Ayam + Sayur",
                    score = "86/100",
                    date = "15/10/2024",
                    onClick = onNavigateToHistory
                )
            }
            item {
                HistoryItem(
                    imageRes = R.drawable.ic_launcher_background,
                    name = "Nasi + Telur",
                    score = "62/100",
                    date = "14/10/2024",
                    onClick = onNavigateToHistory
                )
            }
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
                shape = RoundedCornerShape(50) // Tombol lebih membulat
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

@Composable
fun HistoryItem(
    imageRes: Int,
    name: String,
    score: String,
    date: String,
    onClick: () -> Unit
) {
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
            // Tambahkan Gambar Bulat di Kiri
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(date, fontSize = 12.sp, color = Color.Gray)
            }

            // Skor di Kanan
            Text(score, color = GreenPrimary, fontWeight = FontWeight.Bold)

            // Panah kecil sebagai penanda bisa diklik
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Detail",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun BottomNavBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Scan" to Icons.Default.PhotoCamera,
        "Riwayat" to Icons.Default.DateRange,
        "Progres" to Icons.AutoMirrored.Filled.TrendingUp,
        "Profil" to Icons.Default.Person
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = GreenPrimary
    ) {
        items.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GreenPrimary,
                    selectedTextColor = GreenPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = GreenLight
                )
            )
        }
    }
}