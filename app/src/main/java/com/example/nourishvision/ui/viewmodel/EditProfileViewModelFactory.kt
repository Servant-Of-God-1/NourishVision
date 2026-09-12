// ui/viewmodel/EditProfileViewModelFactory.kt
package com.example.nourishvision.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    // Baca foto dari DataStore (Base64)
    val profileImageBase64: StateFlow<String> = userPreferences.profileImageBase64
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    // Simpan foto (Bitmap)
    fun saveProfileImage(bitmap: Bitmap) {
        viewModelScope.launch {
            userPreferences.saveProfileImage(bitmap)
        }
    }

    // Hapus foto
    fun deleteProfileImage() {
        viewModelScope.launch {
            userPreferences.deleteProfileImage()
        }
    }
}

// Factory
class EditProfileViewModelFactory(
    private val userPreferences: UserPreferences
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}