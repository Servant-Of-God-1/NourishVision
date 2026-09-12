// data/repository/FoodLogRepository.kt
package com.example.nourishvision.data.repository

import com.example.nourishvision.data.model.CreateFoodLogRequest
import com.example.nourishvision.data.model.FoodLogResponse
import com.example.nourishvision.data.remote.RetrofitInstance

class FoodLogRepository {
    suspend fun getFoodLogs(token: String? = null): List<FoodLogResponse> {
        return RetrofitInstance.api.getAllFoodLogs()
    }
    suspend fun getUserFoodLogs(userId: Int): List<FoodLogResponse> {
        return RetrofitInstance.api.getUserFoodLogs(userId)
    }

    suspend fun createFoodLog(request: CreateFoodLogRequest): FoodLogResponse {
        return RetrofitInstance.api.createFoodLog(request)
    }
}