package com.example.nourishvision.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("createdAt") val createdAt: String
)

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// --- Food Log Models ---
data class FoodItem(
    @SerializedName("id") val id: Int,
    @SerializedName("foodLogId") val foodLogId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein") val protein: Double,
    @SerializedName("carbohydrate") val carbohydrate: Double,
    @SerializedName("fat") val fat: Double,
    @SerializedName("fiber") val fiber: Double
)

data class Component(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String
)

data class FoodLogComponent(
    @SerializedName("foodLogId") val foodLogId: Int,
    @SerializedName("componentId") val componentId: Int,
    @SerializedName("component") val component: Component
)

data class FoodItemRequest(
    @SerializedName("name") val name: String,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("calories") val calories: Int,
    @SerializedName("protein") val protein: Double,
    @SerializedName("carbohydrate") val carbohydrate: Double,
    @SerializedName("fat") val fat: Double,
    @SerializedName("fiber") val fiber: Double
)

data class FoodLogResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("recommendation") val recommendation: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("foodItems") val foodItems: List<FoodItem>,
    @SerializedName("components") val components: List<FoodLogComponent>
)

data class CreateFoodLogRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("foods") val foods: List<FoodItemRequest>
)

data class UpdateFoodLogComponentsRequest(
    @SerializedName("componentIds") val componentIds: List<Int>
)