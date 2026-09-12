// data/remote/ApiService.kt
package com.example.nourishvision.data.remote

import com.example.nourishvision.data.model.*
import retrofit2.http.*
import okhttp3.MultipartBody

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    @POST("users")
    suspend fun registerUser(@Body request: RegisterRequest): UserResponse

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): UserResponse

    @GET("food-logs")
    suspend fun getAllFoodLogs(): List<FoodLogResponse>

    @GET("users/{id}/food-logs")
    suspend fun getUserFoodLogs(@Path("id") userId: Int): List<FoodLogResponse>

    @POST("food-logs")
    suspend fun createFoodLog(@Body request: CreateFoodLogRequest): FoodLogResponse

    @GET("food-logs/{id}")
    suspend fun getFoodLogById(@Path("id") id: Int): FoodLogResponse

    @POST("food-logs/{id}/components")
    suspend fun updateFoodLogComponents(
        @Path("id") id: Int,
        @Body request: UpdateFoodLogComponentsRequest
    ): FoodLogResponse
    // --- Components ---
    @GET("components")
    suspend fun getComponents(): List<Component>

    @POST("components")
    suspend fun createComponent(@Body request: Component): Component

    @GET("components/{id}")
    suspend fun getComponentById(@Path("id") id: Int): Component
    @Multipart

    @POST("predict")
    suspend fun predictFood(
        @Part image: MultipartBody.Part
    ): PredictionResponse
}