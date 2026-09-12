package com.example.nourishvision

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.data.repository.AuthRepository
import com.example.nourishvision.data.repository.FoodLogRepository
import com.example.nourishvision.ui.navigation.AppNavigation
import com.example.nourishvision.ui.theme.NourishVisionTheme
import com.example.nourishvision.ui.viewmodel.AuthViewModelFactory
import com.example.nourishvision.ui.viewmodel.FoodLogViewModelFactory
import com.example.nourishvision.ui.viewmodel.EditProfileViewModelFactory
import com.example.nourishvision.ui.viewmodel.UserProfileViewModelFactory
import androidx.compose.runtime.mutableStateOf
import com.example.nourishvision.ui.viewmodel.RegisterViewModelFactory
import com.example.nourishvision.ui.viewmodel.SaveFoodLogViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var authViewModelFactory: AuthViewModelFactory
    private lateinit var foodLogViewModelFactory: FoodLogViewModelFactory
    private lateinit var editProfileViewModelFactory: EditProfileViewModelFactory
    private lateinit var userProfileViewModelFactory: UserProfileViewModelFactory
    private val backCallbackEnabled = mutableStateOf(false)
    private lateinit var registerViewModelFactory: RegisterViewModelFactory
    private lateinit var saveFoodLogViewModelFactory: SaveFoodLogViewModelFactory

    private var backPressedTime = 0L
    private val backPressInterval = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = UserPreferences(applicationContext)
        val authRepository = AuthRepository()
        val foodLogRepository = FoodLogRepository()

        authViewModelFactory = AuthViewModelFactory(authRepository, userPreferences)
        foodLogViewModelFactory = FoodLogViewModelFactory(foodLogRepository, userPreferences)
        editProfileViewModelFactory = EditProfileViewModelFactory(userPreferences)
        userProfileViewModelFactory = UserProfileViewModelFactory(userPreferences)
        registerViewModelFactory = RegisterViewModelFactory(userPreferences)
        saveFoodLogViewModelFactory = SaveFoodLogViewModelFactory(foodLogRepository,userPreferences)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!backCallbackEnabled.value) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                    return
                }

                val currentTime = System.currentTimeMillis()
                if (currentTime - backPressedTime < backPressInterval) {
                    finishAffinity()
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(this@MainActivity, "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT).show()
                }
            }
        })

        setContent {
            NourishVisionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        userPreferences = userPreferences,
                        authViewModelFactory = authViewModelFactory,
                        foodLogViewModelFactory = foodLogViewModelFactory,
                        editProfileViewModelFactory = editProfileViewModelFactory,
                        userProfileViewModelFactory = userProfileViewModelFactory,
                        registerViewModelFactory = registerViewModelFactory,
                        backCallbackEnabled = backCallbackEnabled,
                        saveFoodLogViewModelFactory = saveFoodLogViewModelFactory
                    )
                }
            }
        }
    }
}