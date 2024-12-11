package com.example.nutripal.data.repository

import android.util.Log
import com.example.nutripal.data.local.dao.HistoryDao
import com.example.nutripal.data.local.entity.HistoryItemEntity
import com.example.nutripal.data.model.HistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun fetchAndSaveHistoryItems() {
        val currentUser = auth.currentUser ?: return

        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("nutrition_history")
                    .whereEqualTo("uid", currentUser.uid)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val historyItems = querySnapshot.documents.map { document ->
                    val timestamp = document.getTimestamp("timestamp")?.toDate() ?: return@map null
                    val formattedDateTime = SimpleDateFormat("yyyy-MM-dd | HH.mm", Locale.getDefault()).format(timestamp)

                    HistoryItemEntity(
                        documentId = document.id,
                        uid = currentUser.uid,
                        imageUrl = document.getString("image_url") ?: "https://via.placeholder.com/150",
                        title = "Nutrition Scan",
                        dateTime = formattedDateTime,
                        carbs = "${document.getLong("carbohydrate") ?: 0}g",
                        protein = "${document.getLong("protein") ?: 0}g",
                        fat = "${document.getLong("fat") ?: 0}g",
                        timestamp = timestamp.time,
                        recommendation = document.getString("recommendation")
                    )
                }.filterNotNull()

                historyDao.insertAllHistoryItems(historyItems)
            } catch (e: Exception) {
                Log.e("HistoryRepository", "Error fetching and saving history", e)
            }
        }
    }

    fun getHistoryItems(): Flow<List<HistoryItem>> {
        val currentUser = auth.currentUser ?: return emptyFlow()
        return historyDao.getAllHistoryItems(currentUser.uid)
            .map { entities ->
                entities.map { entity ->
                    HistoryItem(
                        documentId = entity.documentId,
                        imageUrl = entity.imageUrl,
                        title = entity.title,
                        dateTime = entity.dateTime,
                        carbs = entity.carbs,
                        protein = entity.protein,
                        fat = entity.fat
                    )
                }
            }
    }

    suspend fun getHistoryDetails(documentId: String): HistoryItemEntity? {
        return withContext(Dispatchers.IO) {
            historyDao.getHistoryItemById(documentId).first()
        }
    }
}
