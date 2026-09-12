package com.example.nourishvision.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nourishvision.data.model.FoodLogResponse
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary
import com.example.nourishvision.ui.viewmodel.EditProfileViewModel
import com.example.nourishvision.ui.viewmodel.FoodLogViewModel
import com.example.nourishvision.ui.viewmodel.UserProfileViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.example.nourishvision.ui.components.getScoreStatusLabel
import com.example.nourishvision.ui.components.getScoreStatusDescription
import com.example.nourishvision.ui.components.getScoreStatusColor

@Composable
fun HomeScreen(
    onNavigateToBalance: () -> Unit,
    onNavigateToHistory: () -> Unit,
    foodLogViewModel: FoodLogViewModel,
    userProfileViewModel: UserProfileViewModel,
    editProfileViewModel: EditProfileViewModel
) {
    val foodLogs by foodLogViewModel.foodLogs.collectAsState()
    val isLoading by foodLogViewModel.isLoading.collectAsState()
    val localScores by foodLogViewModel.localScores.collectAsState()
    val userName by userProfileViewModel.userName.collectAsState()

    val profileImageBase64 by editProfileViewModel.profileImageBase64.collectAsState()
    val profileBitmap = remember(profileImageBase64) {
        if (profileImageBase64.isNotEmpty()) {
            val bytes = android.util.Base64.decode(profileImageBase64, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else null
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val todayLogs = remember(foodLogs) {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())
        foodLogs.filter { log -> log.createdAt.startsWith(today) }
    }

    val todayScore = remember(todayLogs, localScores) {
        val scores = todayLogs.mapNotNull { log -> localScores[log.id] }
        if (scores.isNotEmpty()) scores.average().toInt() else 0
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == androidx.lifecycle.Lifecycle.State.RESUMED) {
            foodLogViewModel.getFoodLogs()
        }
    }

    Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderHome(userName = userName, profileBitmap = profileBitmap)

            Spacer(modifier = Modifier.height(16.dp))
            FoodBalanceCard(score = todayScore)

            Spacer(modifier = Modifier.height(16.dp))
            SectionTitleWithAction("Riwayat Terbaru", onNavigateToHistory)

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenPrimary)
                }
            } else if (todayLogs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Belum ada riwayat hari ini",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Scan makanan untuk memulai",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(todayLogs) { item ->
                        HomeHistoryItem(
                            item = item,
                            foodBalanceScore = localScores[item.id] ?: 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderHome(userName: String, profileBitmap: android.graphics.Bitmap?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val firstName = userName.split(" ").firstOrNull() ?: userName
            Text("Hi, $firstName!", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text("Hari ini apa yang kamu makan?", fontSize = 14.sp, color = Color.Gray)
        }

        // ✅ Foto profil di kanan atas (hanya display, tidak bisa diklik)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(GreenLight),
            contentAlignment = Alignment.Center
        ) {
            if (profileBitmap != null) {
                AsyncImage(
                    model = profileBitmap,
                    contentDescription = "Foto Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default",
                    tint = GreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

// ui/screens/HomeScreen.kt
@Composable
fun FoodBalanceCard(score: Int) {
    val statusLabel = getScoreStatusLabel(score)
    val statusColor = getScoreStatusColor(score)
    val statusDesc = getScoreStatusDescription(score)

    Card(
        colors = CardDefaults.cardColors(containerColor = GreenLight),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // ============================================================
            // JUDUL
            // ============================================================
            Text(
                text = "Food Balance Score Hari Ini",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ============================================================
            // SKOR BESAR + STATUS
            // ============================================================
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$score",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                )
                Text(
                    text = " / 100",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ============================================================
            // STATUS DENGAN TITIK INDIKATOR
            // ============================================================
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = statusLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ============================================================
            // DESKRIPSI STATUS
            // ============================================================
            Text(
                text = statusDesc,
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // PROGRESS BAR VISUAL
            // ============================================================
            LinearProgressIndicator(
                progress = { (score / 100f).coerceIn(0f, 1f) },
                color = statusColor,
                trackColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
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
fun HomeHistoryItem(item: FoodLogResponse, foodBalanceScore: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageRes = getFoodImageRes(item.imageUrl)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                val displayName = item.foodItems.firstOrNull()?.name
                    ?: item.imageUrl?.replaceFirstChar { it.uppercase() }
                    ?: "Makanan"

                Text(
                    text = displayName,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = item.createdAt.take(10),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "$foodBalanceScore/100",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
fun getTodayStartMillis(): Long {
    val calendar = java.util.Calendar.getInstance()
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}