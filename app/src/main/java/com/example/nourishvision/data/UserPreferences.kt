// data/UserPreferences.kt
package com.example.nourishvision.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val LOCAL_SCORES = stringPreferencesKey("local_scores")
        val PROFILE_IMAGE_BASE64 = stringPreferencesKey("profile_image_base64")
    }

    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_FIRST_TIME] ?: true // Default true (pertama kali)
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    val localScores: Flow<Map<Int, Int>> = context.dataStore.data.map { prefs ->
        val json = prefs[LOCAL_SCORES] ?: "{}"
        try {
            val type = object : TypeToken<Map<Int, Int>>() {}.type
            Gson().fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    suspend fun saveLocalScore(logId: Int, score: Int) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[LOCAL_SCORES] ?: "{}"
            val type = object : TypeToken<MutableMap<Int, Int>>() {}.type
            val currentMap: MutableMap<Int, Int> = try {
                Gson().fromJson(currentJson, type) ?: mutableMapOf()
            } catch (e: Exception) {
                mutableMapOf()
            }
            currentMap[logId] = score
            prefs[LOCAL_SCORES] = Gson().toJson(currentMap)
        }
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[IS_FIRST_TIME] = false
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    val userToken: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_TOKEN] ?: ""
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_TOKEN] = token
        }
    }

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME] ?: "Pengguna"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_EMAIL] ?: "email@example.com"
    }

    val userId: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[USER_ID] ?: 0
    }

    suspend fun saveUserData(id: Int, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
        }
    }

    val profileImageBase64: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PROFILE_IMAGE_BASE64] ?: ""
    }

    suspend fun saveProfileImage(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        context.dataStore.edit { prefs ->
            prefs[PROFILE_IMAGE_BASE64] = base64String
        }
    }

    suspend fun deleteProfileImage() {
        context.dataStore.edit { prefs ->
            prefs.remove(PROFILE_IMAGE_BASE64)
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(IS_LOGGED_IN)
            prefs.remove(USER_TOKEN)
            prefs.remove(USER_ID)
            prefs.remove(USER_NAME)
            prefs.remove(USER_EMAIL)
        }
    }
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}