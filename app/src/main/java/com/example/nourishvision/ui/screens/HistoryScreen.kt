package com.example.nourishvision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.CircleShape
import com.example.nourishvision.ui.theme.GreenLight
import androidx.compose.ui.draw.clip
import com.example.nourishvision.data.model.FoodLogResponse
import com.example.nourishvision.ui.theme.GreenPrimary
import com.example.nourishvision.ui.viewmodel.FoodLogViewModel
import androidx.compose.foundation.background
import com.example.nourishvision.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    foodLogViewModel: FoodLogViewModel
) {
    val foodLogs by foodLogViewModel.foodLogs.collectAsState()
    val isLoading by foodLogViewModel.isLoading.collectAsState()
    val localScores by foodLogViewModel.localScores.collectAsState()
    val errorMessage by foodLogViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        foodLogViewModel.getFoodLogs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Riwayat", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        when {
            isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
            errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(foodLogs) { item ->
                    HistoryCard(item, localScores[item.id] ?: 0)
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: FoodLogResponse, foodBalanceScore: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageRes = getFoodImageRes(item.imageUrl)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = item.foodItems.firstOrNull()?.name ?: "Makanan",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.foodItems.firstOrNull()?.name ?: "Makanan",
                    fontSize = 16.sp,
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
                text = "$foodBalanceScore",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            Text("/100", fontSize = 10.sp, color = Color.Gray)
        }
    }
}
fun getFoodImageRes(imageUrl: String?): Int {
    return when (imageUrl?.lowercase()) {
        "soto" -> R.drawable.soto
        "sate" -> R.drawable.sate
        "nasi_padang" -> R.drawable.nasi_padang
        "nasi_goreng" -> R.drawable.nasi_goreng
        "mie_goreng" -> R.drawable.mie_goreng
        "gado_gado" -> R.drawable.gado_gado
        "bakso" -> R.drawable.bakso
        "ayam_goreng" -> R.drawable.ayam_goreng
        else -> R.drawable.logo
    }
}