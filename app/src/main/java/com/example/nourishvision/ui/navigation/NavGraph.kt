// ui/navigation/NavGraph.kt
package com.example.nourishvision.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.ui.screens.*
import androidx.compose.material3.Text
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.nourishvision.ui.screens.BottomNavBar

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SCAN = "scan"
    const val HISTORY = "history"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"

    const val FOOD_BALANCE = "food_balance"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation(userPreferences: UserPreferences) {
    val navController = rememberNavController()

    // Untuk mendeteksi halaman mana yang sedang aktif
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Daftar halaman utama yang BOLEH memiliki BottomNavBar
    val isMainScreen = currentDestination?.hierarchy?.any { destination ->
        destination.route == Routes.HOME ||
                destination.route == Routes.SCAN ||
                destination.route == Routes.HISTORY ||
                destination.route == Routes.PROGRESS ||
                destination.route == Routes.PROFILE
    } == true

    Scaffold(
        bottomBar = {
            if (isMainScreen) {
                BottomNavBar(
                    currentRoute = currentDestination.route,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    userPreferences = userPreferences,
                    onNavigateToOnboarding = { navController.navigate(Routes.ONBOARDING) { popUpTo(Routes.SPLASH) { inclusive = true } } },
                    onNavigateToHome = { navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } } }
                )
            }
            composable(Routes.ONBOARDING) {
                OnboardingScreen(onNavigateToLogin = { navController.navigate(Routes.LOGIN) })
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    onNavigateToHome = { navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = { navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToBalance = { navController.navigate(Routes.FOOD_BALANCE) },
                    onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                )
            }
            composable(Routes.SCAN) {
                Text("Halaman Scan")
            }
            composable(Routes.HISTORY) {
                HistoryScreen(
                    onBack = { navController.popBackStack() } // Kembali ke Home
                )
            }
            composable(Routes.PROGRESS) {
                Text("Halaman Progress")
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.FOOD_BALANCE) {
                FoodBalanceScreen(
                    onBack = { navController.popBackStack() } // Kembali ke Home atau Scan
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}