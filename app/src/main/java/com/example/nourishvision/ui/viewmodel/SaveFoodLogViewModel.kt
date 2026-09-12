// ui/viewmodel/SaveFoodLogViewModel.kt
package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.model.CreateFoodLogRequest
import com.example.nourishvision.data.model.FoodBalanceData
import com.example.nourishvision.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.nourishvision.data.model.FoodItemRequest
import androidx.lifecycle.ViewModelProvider
import com.example.nourishvision.data.repository.FoodLogRepository

class SaveFoodLogViewModel(
    private val foodLogRepository: FoodLogRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun saveFoodLog(
        userId: Int,
        foodData: FoodBalanceData,
        confidence: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val foodItem = FoodItemRequest(
                    name = foodData.name,
                    confidence = confidence,
                    calories = foodData.calories,
                    protein = foodData.protein,
                    carbohydrate = foodData.carbohydrate,
                    fat = foodData.fat,
                    fiber = foodData.fiber
                )

                val request = CreateFoodLogRequest(
                    userId = userId,
                    imageUrl = getImageUrlForFood(foodData.name),
                    foods = listOf(foodItem)
                )
                val response = foodLogRepository.createFoodLog(request)
                userPreferences.saveLocalScore(
                    logId = response.id,
                    score = foodData.foodBalanceScore
                )
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e("SaveFoodLog", "Error: ${e.message}", e)
                _error.value = e.message ?: "Gagal menyimpan riwayat"
                onError(_error.value!!)
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun getImageUrlForFood(foodName: String): String {
        return when (foodName.lowercase()) {
            "soto" -> "soto"
            "sate" -> "sate"
            "nasi padang" -> "nasi_padang"
            "nasi goreng" -> "nasi_goreng"
            "mie goreng" -> "mie_goreng"
            "gado-gado" -> "gado_gado"
            "bakso" -> "bakso"
            "ayam goreng" -> "ayam_goreng"
            else -> "default_food"
        }
    }
}

// Factory
class SaveFoodLogViewModelFactory(
    private val foodLogRepository: FoodLogRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveFoodLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SaveFoodLogViewModel(foodLogRepository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}