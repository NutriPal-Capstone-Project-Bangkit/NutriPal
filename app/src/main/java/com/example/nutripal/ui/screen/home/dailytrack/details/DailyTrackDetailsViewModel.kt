package com.example.nutripal.ui.screen.home.dailytrack.details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.NutritionFacts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DailyTrackDetailsViewModel : ViewModel() {
    private val _nutritionFacts = MutableStateFlow<NutritionFacts?>(null)
    val nutritionFacts: StateFlow<NutritionFacts?> = _nutritionFacts.asStateFlow()

    private val _recommendation = MutableStateFlow<String?>(null)
    val recommendation: StateFlow<String?> = _recommendation.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _product = MutableStateFlow<String?>(null)
    val product: StateFlow<String?> = _product.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()

    fun fetchHistoryDetails(id: String) {
        viewModelScope.launch {
            try {
                // Query the daily_nutritions collection where the 'id' field matches
                val querySnapshot = firestore.collection("daily_nutritions")
                    .whereEqualTo("id", id)
                    .limit(1)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()

                    // Extract nutrition facts
                    val carbs = document.getLong("carbohydrate")?.toInt() ?: 0
                    val protein = document.getLong("protein")?.toInt() ?: 0
                    val fat = document.getLong("fat")?.toInt() ?: 0

                    _nutritionFacts.value = NutritionFacts(carbs, protein, fat)

                    // Set recommendation
                    _recommendation.value = document.getString("recommendation")
                        ?: "No specific recommendation available"

                    // Set product name
                    _product.value = document.getString("product") ?: "Unknown Product"

                    // Extract image URI
                    val imageUriString = document.getString("image")
                    imageUriString?.let {
                        _imageUri.value = Uri.parse(it)
                    }

                    Log.d("DailyTrackDetailsViewModel", "Successfully fetched details for id: $id")
                } else {
                    Log.e("DailyTrackDetailsViewModel", "No document found with id: $id")
                    // Reset all values
                    _nutritionFacts.value = null
                    _recommendation.value = null
                    _imageUri.value = null
                    _product.value = null
                }
            } catch (e: Exception) {
                Log.e("DailyTrackDetailsViewModel", "Error fetching details", e)
                // Reset all values in case of error
                _nutritionFacts.value = null
                _recommendation.value = null
                _imageUri.value = null
                _product.value = null
            }
        }
    }
}