package com.example.nourishvision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.UserPreferences
import com.example.nourishvision.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import coil.util.CoilUtils.result

class AuthViewModel(
    private val repo: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = repo.login(email, password)
                if (user != null) {
                    // Simpan data user ke DataStore
                    userPreferences.saveUserData(
                        id = user.id,
                        name = user.name,
                        email = user.email
                    )
                    userPreferences.setLoggedIn(true)
                    onSuccess()
                } else {
                    _error.value = "Email tidak terdaftar. Silakan daftar terlebih dahulu."
                }
            } catch (e: Exception) {
                _error.value = "Gagal login: ${e.message ?: "Kesalahan jaringan"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fungsi Register (POST /users)
    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val user = repo.registerUser(name, email, password)
                userPreferences.saveUserId(user.id)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.logout()
            onSuccess()
        }
    }
    fun delete(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearAll()
            onSuccess()
        }
    }
}