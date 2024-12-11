package com.example.nutripal.ui.screen.home.dailytrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class DailyNutritionItem(
    val id: String = "",
    val image: String = "",
    val product: String = "",
    val timestamp: String = "",
    val carbohydrate: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val recommendation: String = ""
)

class DailyNutritionTrackViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _nutritionItems = MutableStateFlow<List<DailyNutritionItem>>(emptyList())
    val nutritionItems: StateFlow<List<DailyNutritionItem>> = _nutritionItems.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        println("ViewModel initialized. Fetching nutrition items for current date.")
        fetchNutritionItemsForCurrentDate()
    }

    fun updateSelectedDate(date: LocalDate) {
        println("updateSelectedDate called with date: $date")
        if (_selectedDate.value != date) {
            _selectedDate.value = date
            println("Date updated to: $date")
            fetchNutritionData()
        } else {
            println("Date unchanged: $date")
        }
    }

    private fun fetchNutritionData() {
        println("fetchNutritionData called.")
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: run {
                println("No user ID found. Exiting fetchNutritionData.")
                return@launch
            }

            val startOfDay = _selectedDate.value.atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant()
            val endOfDay = startOfDay.plusSeconds(86400)

            println("Fetching nutrition data from $startOfDay to $endOfDay")

            // Fetch data from "daily_nutrition" collection
            val dailyNutritionQuery = firestore.collection("daily_nutritions")
                .whereEqualTo("uid", userId)
                .whereGreaterThanOrEqualTo("timestamp", com.google.firebase.Timestamp(startOfDay.toEpochMilli() / 1000, 0))
                .whereLessThan("timestamp", com.google.firebase.Timestamp(endOfDay.toEpochMilli() / 1000, 0))

            try {
                val dailyNutritionSnapshot = dailyNutritionQuery.get().await()

                val dailyNutritionItems = dailyNutritionSnapshot.documents.mapNotNull { document ->
                    val timestamp = document.getTimestamp("timestamp")
                    if (timestamp != null &&
                        timestamp.toDate().time >= startOfDay.toEpochMilli() &&
                        timestamp.toDate().time < endOfDay.toEpochMilli()
                    ) {
                        DailyNutritionItem(
                            id = document.getString("id") ?: "",
                            image = document.getString("image") ?: "",
                            product = document.getString("product") ?: "",
                            timestamp = formatTimestamp(timestamp.toDate().time),
                            carbohydrate = document.getLong("carbohydrate")?.toInt() ?: 0,
                            protein = document.getLong("protein")?.toInt() ?: 0,
                            fat = document.getLong("fat")?.toInt() ?: 0,
                            recommendation = document.getString("recommendation") ?: ""
                        )
                    } else {
                        null
                    }
                }

                println("Fetched ${dailyNutritionItems.size} nutrition items.")
                _nutritionItems.value = dailyNutritionItems

            } catch (exception: Exception) {
                println("Error fetching data: ${exception.message}")
                _nutritionItems.value = emptyList()
            }
        }
    }

    private fun fetchNutritionItemsForCurrentDate() {
        println("fetchNutritionItemsForCurrentDate called.")
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            println("No user ID found. Setting nutrition items to empty list.")
            _nutritionItems.value = emptyList()
            return
        }

        viewModelScope.launch {
            // Calculate start and end of the selected day
            val startOfDay = _selectedDate.value.atStartOfDay(ZoneId.of("Asia/Jakarta")).toInstant()
            val endOfDay = startOfDay.plusSeconds(86400)

            println("Fetching nutrition items for user $currentUserId from $startOfDay to $endOfDay")

            firestore.collection("daily_nutritions")
                .whereEqualTo("uid", currentUserId)
                .whereGreaterThanOrEqualTo("timestamp", com.google.firebase.Timestamp(startOfDay.toEpochMilli() / 1000, 0))
                .whereLessThan("timestamp", com.google.firebase.Timestamp(endOfDay.toEpochMilli() / 1000, 0))
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val items = querySnapshot.documents.mapNotNull { document ->
                        val timestamp = document.getTimestamp("timestamp")
                        if (timestamp != null &&
                            timestamp.toDate().time >= startOfDay.toEpochMilli() &&
                            timestamp.toDate().time < endOfDay.toEpochMilli()
                        ) {
                            println("Nutrition item found with ID: ${document.id}")
                            DailyNutritionItem(
                                id = document.getString("id") ?: "",
                                image = document.getString("image") ?: "",
                                product = document.getString("product") ?: "",
                                timestamp = formatTimestamp(timestamp.toDate().time),
                                carbohydrate = document.getLong("carbohydrate")?.toInt() ?: 0,
                                protein = document.getLong("protein")?.toInt() ?: 0,
                                fat = document.getLong("fat")?.toInt() ?: 0,
                                recommendation = document.getString("recommendation") ?: ""
                            )
                        } else {
                            println("Nutrition item with ID: ${document.id} does not match the criteria.")
                            null
                        }
                    }
                    println("Fetched ${items.size} nutrition items for the current date.")
                    _nutritionItems.value = items
                }
                .addOnFailureListener {
                    println("Error fetching nutrition items: ${it.message}")
                    _nutritionItems.value = emptyList()
                }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        println("formatTimestamp called with timestamp: $timestamp")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd '|' HH:mm", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val formattedTimestamp = dateFormat.format(Date(timestamp))
        println("Formatted timestamp: $formattedTimestamp")
        return formattedTimestamp
    }

    fun getMonthName(date: LocalDate): String {
        println("getMonthName called with date: $date")
        val monthName = date.format(DateTimeFormatter.ofPattern("MMM"))
        println("Month name for date $date is $monthName")
        return monthName
    }
}
