package com.example.nourishvision.data.model

data class FoodBalanceData(
    val name: String,
    val calories: Int,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double,
    val fiber: Double,
    val foodBalanceScore: Int,
    val description: String
)