package com.example.nourishvision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.nourishvision.R
import com.example.nourishvision.data.UserPreferences
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.activity.compose.BackHandler

@Composable
fun SplashScreen(
    userPreferences: UserPreferences,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val isFirstTime by userPreferences.isFirstTime.collectAsState(initial = true)
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
    val context = LocalContext.current
    BackHandler {
        (context as? android.app.Activity)?.finishAffinity()
    }

    LaunchedEffect(isFirstTime, isLoggedIn) {
        delay(2000)
        when {
            isFirstTime -> onNavigateToOnboarding()
            isLoggedIn -> onNavigateToHome()
            else -> onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White).statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo NourishVision",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.merek),
                contentDescription = "Nama Merek NourishVision",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}