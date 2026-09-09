// ui/navigation/NavGraph.kt
package com.example.nourishvision.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.ui.screens.*

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavigation(userPreferences: UserPreferences) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                userPreferences = userPreferences,
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) { popUpTo(Routes.SPLASH) { inclusive = true } }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                },
                // Panggil fungsi ini saat user menekan "Mulai Sekarang" di akhir onboarding
                onOnboardingFinished = {
                    // Tandai sudah tidak pertama kali
                    // Anda perlu memanggil fungsi ini dari ViewModel atau scope di OnboardingScreen
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(onNavigateToBalance = { /* Navigasi ke Balance */ })
        }
    }
}