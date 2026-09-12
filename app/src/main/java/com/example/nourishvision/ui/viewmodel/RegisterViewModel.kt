// ui/viewmodel/RegisterViewModel.kt
package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.data.model.RegisterRequest
import com.example.nourishvision.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // 1. Panggil API register
                val user = RetrofitInstance.api.registerUser(
                    RegisterRequest(name = name, email = email, password = password)
                )

                // 2. Simpan data user ke DataStore
                userPreferences.saveUserData(
                    id = user.id,
                    name = user.name,
                    email = user.email
                )

                // 3. Tandai user sudah login
                userPreferences.setLoggedIn(true)

                onSuccess()
            } catch (e: Exception) {
                _error.value = "Gagal daftar: ${e.message ?: "Kesalahan jaringan"}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Factory
class RegisterViewModelFactory(
    private val userPreferences: UserPreferences
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}