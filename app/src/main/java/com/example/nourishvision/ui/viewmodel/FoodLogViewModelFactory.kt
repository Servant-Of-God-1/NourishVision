// ui/viewmodel/FoodLogViewModelFactory.kt
package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.data.repository.FoodLogRepository

class FoodLogViewModelFactory(
    private val repository: FoodLogRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodLogViewModel(repository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}