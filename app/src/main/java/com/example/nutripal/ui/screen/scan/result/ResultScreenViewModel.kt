package com.example.nutripal.ui.screen.scan.result

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.repository.OCRRepository
import com.example.nutripal.data.model.NutritionFacts
import kotlinx.coroutines.launch

class ResultScreenViewModel(private val ocrRepository: OCRRepository) : ViewModel() {
    var nutritionFacts: NutritionFacts? by mutableStateOf(null)
        private set

    fun extractNutritionFromBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            val extractedValues = ocrRepository.extractNutritionalValues(bitmap)
            nutritionFacts = NutritionFacts(
                carbohydrate = extractedValues["Karbohidrat"]?.toIntOrNull() ?: 0,
                protein = extractedValues["Protein"]?.toIntOrNull() ?: 0,
                fat = extractedValues["Lemak"]?.toIntOrNull() ?: 0
            )
            Log.d("ResultScreenViewModel", "Created NutritionFacts: $nutritionFacts")
        }
    }
}
