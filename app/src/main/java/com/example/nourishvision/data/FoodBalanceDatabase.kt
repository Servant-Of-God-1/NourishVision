package com.example.nourishvision.data

import com.example.nourishvision.data.model.FoodBalanceData

object FoodBalanceDatabase {

    private val foodDatabase = mapOf(
        "Gado-gado" to FoodBalanceData(
            name = "Gado-gado",
            calories = 320,
            carbohydrate = 30.0,
            protein = 14.0,
            fat = 15.0,
            fiber = 7.0,
            foodBalanceScore = 88,
            description = "Sangat sehat dan kaya serat dari sayuran. Sangat sesuai dengan panduan gizi seimbang WHO."
        ),
        "Soto" to FoodBalanceData(
            name = "Soto",
            calories = 350,
            carbohydrate = 35.0,
            protein = 20.0,
            fat = 12.0,
            fiber = 3.0,
            foodBalanceScore = 75,
            description = "Kaya protein dari daging dan kaldu. Tambahkan sayuran extra untuk meningkatkan kadar serat."
        ),
        "Bakso" to FoodBalanceData(
            name = "Bakso",
            calories = 300,
            carbohydrate = 25.0,
            protein = 18.0,
            fat = 12.0,
            fiber = 1.0,
            foodBalanceScore = 68,
            description = "Tinggi protein, namun perhatikan kandungan natrium (garam) pada kuah dan olahan daging."
        ),
        "Sate" to FoodBalanceData(
            name = "Sate",
            calories = 420,
            carbohydrate = 20.0,
            protein = 30.0,
            fat = 22.0,
            fiber = 2.0,
            foodBalanceScore = 65,
            description = "Tinggi protein, namun bumbu kacang menambah asupan lemak. Imbangi dengan acar atau lalapan."
        ),
        "Ayam Goreng" to FoodBalanceData(
            name = "Ayam Goreng",
            calories = 380,
            carbohydrate = 10.0,
            protein = 30.0,
            fat = 25.0,
            fiber = 0.5,
            foodBalanceScore = 62,
            description = "Tinggi protein, tetapi kandungan lemak jenuh cukup tinggi akibat proses penggorengan."
        ),
        "Nasi Padang" to FoodBalanceData(
            name = "Nasi Padang",
            calories = 650,
            carbohydrate = 85.0,
            protein = 28.0,
            fat = 25.0,
            fiber = 4.0,
            foodBalanceScore = 60,
            description = "Padat energi dan protein, namun tinggi kalori serta lemak jenuh dari olahan santan dan minyak."
        ),
        "Nasi Goreng" to FoodBalanceData(
            name = "Nasi Goreng",
            calories = 540,
            carbohydrate = 70.0,
            protein = 15.0,
            fat = 18.0,
            fiber = 2.0,
            foodBalanceScore = 58,
            description = "Dominan karbohidrat dan minyak. Disarankan menambah telur dan sayuran segar agar lebih seimbang."
        ),
        "Mie Goreng" to FoodBalanceData(
            name = "Mie Goreng",
            calories = 470,
            carbohydrate = 60.0,
            protein = 12.0,
            fat = 20.0,
            fiber = 1.5,
            foodBalanceScore = 55,
            description = "Tinggi karbohidrat olahan dan natrium dengan serat yang rendah. Batasi konsumsi berlebihan."
        )
    )

    fun getFoodData(foodName: String?): FoodBalanceData {
        if (foodName.isNullOrBlank()) return defaultFoodData

        foodDatabase[foodName]?.let { return it }

        foodDatabase.entries.firstOrNull {
            it.key.equals(foodName, ignoreCase = true)
        }?.let { return it.value }

        return defaultFoodData.copy(name = foodName)
    }

    private val defaultFoodData = FoodBalanceData(
        name = "Makanan",
        calories = 400,
        carbohydrate = 50.0,
        protein = 15.0,
        fat = 15.0,
        fiber = 2.0,
        foodBalanceScore = 65,
        description = "Informasi gizi belum tersedia untuk makanan ini."
    )
}