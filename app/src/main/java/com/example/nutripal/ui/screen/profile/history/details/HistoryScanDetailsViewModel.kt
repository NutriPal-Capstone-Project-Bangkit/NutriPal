package com.example.nutripal.ui.screen.profile.history.details

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScanDetailsViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _nutritionFacts = MutableStateFlow<NutritionFacts?>(null)
    val nutritionFacts: StateFlow<NutritionFacts?> = _nutritionFacts.asStateFlow()

    private val _recommendation = MutableStateFlow<String?>(null)
    val recommendation: StateFlow<String?> = _recommendation.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    fun fetchHistoryDetails(documentId: String) {
        viewModelScope.launch {
            val historyItem = historyRepository.getHistoryDetails(documentId).first()
            historyItem?.let { item ->
                _nutritionFacts.value = NutritionFacts(
                    carbohydrate = item.carbs?.replace("g", "")?.toIntOrNull() ?: 0,
                    protein = item.protein?.replace("g", "")?.toIntOrNull() ?: 0,
                    fat = item.fat?.replace("g", "")?.toIntOrNull() ?: 0
                )
                _recommendation.value = item.recommendation ?: "No specific recommendation available"
                _imageUri.value = Uri.parse(item.imageUrl)
            }
        }
    }
}