package com.example.nutripal.data.model

data class HistoryItem(
    val documentId: String,
    val imageUrl: String = "https://via.placeholder.com/150",
    val title: String = "Nutrition Scan",
    val dateTime: String,
    val carbs: String,
    val protein: String,
    val fat: String
)