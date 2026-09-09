// ui/screens/OnboardingScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Belajar Gizi, Mulai dari Makananmu Sehari-hari",
                    desc = "Foto makananmu dan dapatkan analisis gizi secara real-time.",
                    iconColor = GreenPrimary
                )
                1 -> OnboardingPage(
                    title = "Kenali Kandungan Gizi",
                    desc = "Analisis kalori, protein, lemak, dan karbohidrat dengan teknologi AI.",
                    iconColor = GreenPrimary
                )
                2 -> OnboardingPage(
                    title = "Sertifikasinya",
                    desc = "Dapatkan skor keseimbangan gizi untuk hidup lebih sehat.",
                    iconColor = GreenPrimary
                )
            }
        }

        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
                            color = if (isSelected) GreenPrimary else Color.Gray.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }

        // Tombol Navigasi
        Button(
            onClick = {
                if (pagerState.currentPage < 2) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    scope.launch {
                        userPreferences.setOnboardingCompleted()
                        onNavigateToLogin()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = if (pagerState.currentPage < 2) "Selanjutnya" else "Mulai Sekarang",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun OnboardingPage(title: String, desc: String, iconColor: Color) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(GreenLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🍽️", fontSize = 80.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = desc,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            lineHeight = 24.sp
        )
    }
}