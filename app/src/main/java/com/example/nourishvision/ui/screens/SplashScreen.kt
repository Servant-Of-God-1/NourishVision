package com.example.nourishvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.nourishvision.R
import com.example.nourishvision.data.UserPreferences
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    userPreferences: UserPreferences,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        delay(2000)
        userPreferences.isFirstTime.collect { isFirstTime ->
            if (isFirstTime) {
                onNavigateToOnboarding()
            } else {
                userPreferences.isLoggedIn.collect { isLoggedIn ->
                    if (isLoggedIn) {
                        onNavigateToHome()
                    } else {
                        onNavigateToOnboarding()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // UI Logo Anda
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(0.6f),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.merek),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth(0.6f),
            contentScale = ContentScale.Fit
        )
    }
}