package com.example.nourishvision.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Ekstensi untuk Context agar mudah diakses
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // Membaca status apakah pertama kali
    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_FIRST_TIME] ?: true // Default-nya true (pertama kali) jika belum disimpan
    }

    // Membaca status apakah sudah login
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Menyimpan bahwa user sudah melewati onboarding
    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = false
        }
    }

    // Menyimpan status login
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }
}