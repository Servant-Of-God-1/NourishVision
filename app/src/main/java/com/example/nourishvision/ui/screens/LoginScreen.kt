// ui/screens/LoginScreen.kt
package com.example.nourishvision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nourishvision.ui.theme.GreenPrimary
import com.example.nourishvision.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo dan Judul
        Text("Selamat Datang!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Silakan masuk ke akun Anda untuk melanjutkan",
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Masukkan email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Masukkan password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        // Lupa Password
        TextButton(
            onClick = { /* Navigasi ke lupa password */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Lupa password?", color = GreenPrimary, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Login
        Button(
            onClick = onNavigateToHome,
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pemisah "atau"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
            Text(" atau ", modifier = Modifier.padding(horizontal = 12.dp), color = Color.Gray)
            Divider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.3f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Google (Outlined)
        OutlinedButton(
            onClick = { /* Login dengan Google */ },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Login dengan Google",
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            // Untuk ikon Google, Anda bisa menambahkan Image(painter = painterResource(id = R.drawable.google_logo), ...) di sini
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link Daftar
        Row {
            Text("Belum punya akun?", color = Color.Gray)
            TextButton(onClick = onNavigateToRegister) {
                Text("Daftar di sini", color = GreenPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}