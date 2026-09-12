package com.example.nourishvision.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.ui.screens.*
import com.example.nourishvision.ui.viewmodel.AuthViewModel
import com.example.nourishvision.ui.viewmodel.AuthViewModelFactory
import com.example.nourishvision.ui.viewmodel.EditProfileViewModel
import com.example.nourishvision.ui.viewmodel.EditProfileViewModelFactory
import com.example.nourishvision.ui.viewmodel.FoodLogViewModel
import com.example.nourishvision.ui.viewmodel.FoodLogViewModelFactory
import com.example.nourishvision.ui.viewmodel.RegisterViewModel
import com.example.nourishvision.ui.viewmodel.RegisterViewModelFactory
import com.example.nourishvision.ui.viewmodel.UserProfileViewModel
import com.example.nourishvision.ui.viewmodel.UserProfileViewModelFactory
import com.example.nourishvision.ui.viewmodel.ScanViewModel
import androidx.compose.runtime.remember
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.nourishvision.ui.viewmodel.SaveFoodLogViewModel
import com.example.nourishvision.ui.viewmodel.SaveFoodLogViewModelFactory

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val HISTORY = "history"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val SCAN = "scan"
    const val PREDICTION_RESULT = "prediction_result"
    const val FOOD_BALANCE = "food_balance/{foodname}/{confidence}"
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"
    fun foodBalance(foodName: String, confidence: Double) =
        "food_balance/$foodName/$confidence"
}

@Composable
fun AppNavigation(
    userPreferences: UserPreferences,
    authViewModelFactory: AuthViewModelFactory,
    foodLogViewModelFactory: FoodLogViewModelFactory,
    editProfileViewModelFactory: EditProfileViewModelFactory,
    userProfileViewModelFactory: UserProfileViewModelFactory,
    registerViewModelFactory: RegisterViewModelFactory,
    saveFoodLogViewModelFactory: SaveFoodLogViewModelFactory,
    backCallbackEnabled: MutableState<Boolean>
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val mainScreens = listOf(
        Routes.HOME,
        Routes.HISTORY,
        Routes.PROGRESS,
        Routes.PROFILE,
        Routes.SCAN
    )

    val isMainScreen = currentDestination?.hierarchy?.any { destination ->
        mainScreens.contains(destination.route)
    } == true
    LaunchedEffect(currentRoute) {
        backCallbackEnabled.value = mainScreens.contains(currentRoute)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (isMainScreen) {
                BottomNavBar(
                    currentRoute = currentRoute,
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    userPreferences = userPreferences,
                    onNavigateToOnboarding = {
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
                )
            }

            composable(Routes.LOGIN) {
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                    authViewModel = authViewModel
                )
            }

            composable(Routes.REGISTER) {
                val registerViewModel: RegisterViewModel = viewModel(
                    factory = registerViewModelFactory
                )
                RegisterScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    registerViewModel = registerViewModel
                )
            }

            composable(Routes.HOME) {
                val foodLogViewModel: FoodLogViewModel = viewModel(factory = foodLogViewModelFactory)
                val userProfileViewModel: UserProfileViewModel = viewModel(factory = userProfileViewModelFactory)
                val editProfileViewModel: EditProfileViewModel = viewModel(factory = editProfileViewModelFactory)

                LaunchedEffect(Unit) {
                    foodLogViewModel.getFoodLogs()
                }

                HomeScreen(
                    onNavigateToBalance = { navController.navigate(Routes.FOOD_BALANCE) },
                    onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                    foodLogViewModel = foodLogViewModel,
                    userProfileViewModel = userProfileViewModel,
                    editProfileViewModel = editProfileViewModel
                )
            }
            composable(Routes.HISTORY) {
                val foodLogViewModel: FoodLogViewModel = viewModel(factory = foodLogViewModelFactory)
                HistoryScreen(
                    onBack = { navController.popBackStack() },
                    foodLogViewModel = foodLogViewModel
                )
            }
            composable(
                route = "food_balance/{foodName}/{confidence}",
                arguments = listOf(
                    navArgument("foodName") { type = NavType.StringType },
                    navArgument("confidence") { type = NavType.FloatType }
                )
            ) { backStackEntry ->
                val foodName = backStackEntry.arguments?.getString("foodName") ?: "Makanan"
                val confidence = backStackEntry.arguments?.getFloat("confidence")?.toDouble() ?: 0.0

                val userProfileViewModel: UserProfileViewModel = viewModel(
                    factory = userProfileViewModelFactory
                )
                val userId by userProfileViewModel.userId.collectAsState(initial = 0)

                val saveViewModel: SaveFoodLogViewModel = viewModel(factory = saveFoodLogViewModelFactory)

                FoodBalanceScreen(
                    foodName = foodName,
                    confidence = confidence,
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    onSaveHistory = { foodData ->
                        saveViewModel.saveFoodLog(
                            userId = userId,
                            foodData = foodData,
                            confidence = confidence,
                            onSuccess = {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.HOME) { inclusive = true }
                                }
                            },
                            onError = { message ->
                                android.util.Log.e("SaveFoodLog", "Error: $message")
                            }
                        )
                    },
                )
            }
            composable(Routes.PROFILE) {
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
                val editProfileViewModel: EditProfileViewModel = viewModel(
                    factory = editProfileViewModelFactory
                )
                val userProfileViewModel: UserProfileViewModel = viewModel(
                    factory = userProfileViewModelFactory
                )
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                    onNavigateToOnboarding = { navController.navigate(Routes.ONBOARDING) },
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    authViewModel = authViewModel,
                    editProfileViewModel = editProfileViewModel,
                    userProfileViewModel = userProfileViewModel
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToEditProfile = { navController.navigate(Routes.EDIT_PROFILE) }
                )
            }

            composable(Routes.EDIT_PROFILE) {
                val editProfileViewModel: EditProfileViewModel = viewModel(
                    factory = editProfileViewModelFactory
                )
                val userProfileViewModel: UserProfileViewModel = viewModel(
                    factory = userProfileViewModelFactory
                )
                val userName by userProfileViewModel.userName.collectAsState()

                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    editProfileViewModel = editProfileViewModel,
                    userName = userName
                )
            }

            composable(Routes.SCAN) { backStackEntry ->
                val scanViewModel: ScanViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry
                )

                ScanScreen(
                    onPredictionResult = {
                        navController.navigate(Routes.PREDICTION_RESULT)
                    },
                    scanViewModel = scanViewModel
                )
            }

            composable(Routes.PREDICTION_RESULT) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.SCAN)
                }
                val scanViewModel: ScanViewModel = viewModel(
                    viewModelStoreOwner = parentEntry
                )
                val prediction by scanViewModel.prediction.collectAsState()

                PredictionResultScreen(
                    prediction = prediction,
                    onNavigateToScanner = {
                        scanViewModel.resetPrediction()
                        navController.popBackStack()
                    },
                    onNavigateToBalance = { foodName, confidence ->
                        scanViewModel.resetPrediction()
                        navController.navigate(Routes.foodBalance(foodName, confidence))
                    }
                )
            }

            composable(Routes.PROGRESS) {
                val foodLogViewModel: FoodLogViewModel = viewModel(factory = foodLogViewModelFactory)
                ProgressScreen(
                    onBack = { navController.popBackStack() },
                    foodLogViewModel = foodLogViewModel
                )
            }
        }
    }
}