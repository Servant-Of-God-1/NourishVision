// ui/viewmodel/FoodLogViewModel.kt
package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.data.model.FoodLogResponse
import com.example.nourishvision.data.repository.FoodLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FoodLogViewModel(
    private val repo: FoodLogRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _foodLogs = MutableStateFlow<List<FoodLogResponse>>(emptyList())
    val foodLogs: StateFlow<List<FoodLogResponse>> = _foodLogs

    private val _localScores = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val localScores: StateFlow<Map<Int, Int>> = _localScores


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            userPreferences.localScores.collect { scores ->
                _localScores.value = scores
            }
        }
    }

    fun getFoodLogs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = userPreferences.userId.first()
                _foodLogs.value = repo.getUserFoodLogs(userId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal mengambil data"
            } finally {
                _isLoading.value = false
            }
        }
    }
}