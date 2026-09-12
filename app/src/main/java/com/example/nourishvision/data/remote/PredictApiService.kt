package com.example.nourishvision.data.remote

import com.example.nourishvision.data.model.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PredictApiService {
    @Multipart
    @POST("predict")
    suspend fun predictFood(
        @Part image: MultipartBody.Part
    ): PredictionResponse
}