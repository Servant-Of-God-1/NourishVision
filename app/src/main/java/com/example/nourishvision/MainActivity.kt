package com.example.nourishvision

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.nourishvision.ui.navigation.AppNavigation
import com.example.nourishvision.ui.theme.NourishVisionTheme
import com.example.nourishvision.data.UserPreferences

class MainActivity : ComponentActivity() {
    private var backPressedTime: Long = 0
    private val backPressInterval: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userPreferences = UserPreferences(applicationContext)

        setContent {
            NourishVisionTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(userPreferences = userPreferences)
                }
            }
        }
    }
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < backPressInterval) {
            finishAffinity()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
    }
}