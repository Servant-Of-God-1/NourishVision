package com.example.nourishvision.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nourishvision.data.model.PredictionResponse
import com.example.nourishvision.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ScanViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _prediction = MutableStateFlow<PredictionResponse?>(null)
    val prediction: StateFlow<PredictionResponse?> = _prediction

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun predict(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _prediction.value = null
            try {
                val file = uriToFile(context, imageUri)
                android.util.Log.d("ScanDebug", "File size: ${file.length()} bytes") // ✅ Log ukuran

                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = file.name,
                    body = requestBody
                )

                val result = RetrofitInstance.predictApi.predictFood(multipartBody)

                android.util.Log.d("ScanDebug", "===== RESPONSE DITERIMA =====")
                android.util.Log.d("ScanDebug", "Food: '${result.food}'")
                android.util.Log.d("ScanDebug", "Confidence: ${result.confidence}")
                android.util.Log.d("ScanDebug", "==============================")

                _prediction.value = result
            } catch (e: Exception) {
                android.util.Log.e("ScanDebug", "Error: ${e.message}", e) // ✅ Log error lengkap
                _error.value = "Gagal memprediksi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Tidak bisa membuka gambar")
        val tempFile = File(context.cacheDir, "scan_${System.currentTimeMillis()}.jpg")
        FileOutputStream(tempFile).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        return tempFile
    }

    fun resetPrediction() {
        _prediction.value = null
        _error.value = null
    }
}