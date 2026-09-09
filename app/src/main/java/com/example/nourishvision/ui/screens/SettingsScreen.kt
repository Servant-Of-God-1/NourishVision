// ui/screens/SettingsScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.ui.theme.GreenLight
import com.example.nourishvision.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            // --- Bagian 1: Akun (Ubah Data Profil & Password) ---
            Text("Akun", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            SettingsMenuItem(
                icon = Icons.Default.Person,
                title = "Ubah Data Profil",
                subtitle = "Nama, Email, dan Foto Profil",
                onClick = { /* Navigasi ke halaman Edit Profile */ }
            )

            SettingsMenuItem(
                icon = Icons.Default.Lock,
                title = "Ubah Password",
                subtitle = "Perbarui kata sandi akun Anda",
                onClick = { /* Navigasi ke halaman Change Password */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Bagian 2: Tampilan (Tema) ---
            Text("Tampilan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            SettingsMenuItem(
                icon = Icons.Default.Palette,
                title = "Tema Aplikasi",
                subtitle = "Sesuaikan warna dan tampilan aplikasi",
                onClick = { /* Navigasi ke pengaturan tema */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Bagian 3: Tentang Aplikasi (Versi) ---
            Text("Tentang Aplikasi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "Versi Aplikasi",
                subtitle = "NourishVision v1.0.0", // Versi aplikasi
                onClick = { /* Menampilkan dialog info versi */ }
            )
        }
    }
}

// Komponen Reusable untuk Settings Menu
@Composable
fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Sedikit bayangan
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lingkaran kecil untuk ikon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(GreenLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Panah",
                tint = Color.Gray
            )
        }
    }
}