package com.example.nourishvision.data.model

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("food") val food: String,
    @SerializedName("confidence") val confidence: Double
)