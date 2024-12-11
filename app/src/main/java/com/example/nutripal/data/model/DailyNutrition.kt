package com.example.nutripal.data.model

import com.google.firebase.Timestamp

data class DailyNutrition(
    val id: String = "",
    val uid: String = "",
    val product: String = "",
    val image: String = "",
    val protein: Int = 0,
    val fat: Int = 0,
    val carbohydrate: Int = 0,
    val recommendation: String = "",
    val timestamp: Timestamp
)
