package com.example.nutripal.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_history")
data class HistoryItemEntity(
    @PrimaryKey val documentId: String,
    val uid: String,
    val imageUrl: String,
    val title: String,
    val dateTime: String,
    val carbs: String,
    val protein: String,
    val fat: String,
    val timestamp: Long,
    val recommendation: String?
)