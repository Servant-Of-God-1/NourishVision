// ui/viewmodel/UserProfileViewModel.kt
package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class UserProfileViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val userId: StateFlow<Int> = userPreferences.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Pengguna"
        )

    val userEmail: StateFlow<String> = userPreferences.userEmail
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "email@example.com"
        )
}

// Factory
class UserProfileViewModelFactory(
    private val userPreferences: UserPreferences
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}