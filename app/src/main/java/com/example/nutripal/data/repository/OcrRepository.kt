package com.example.nutripal.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OCRRepository @Inject constructor() {

    suspend fun extractNutritionalValues(bitmap: Bitmap): Map<String, String> {
        return withContext(Dispatchers.IO) {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            val result = mutableMapOf<String, String>()
            val intermediateNumericValues = mutableListOf<Pair<String, String>>()

            try {
                val visionText = recognizer.process(inputImage).await()
                val text = visionText.text
                Log.d("OCRRepository", "Recognized text: $text")

                val lines = text.split("\n")
                val nutritionalData = mutableMapOf<String, String>()

                for (line in lines) {
                    Log.d("OCRRepository", "Processing line: $line")

                    when {
                        line.contains("Lemak Total", ignoreCase = true) -> {
                            val fatValue = extractValueWithContext(line, "Lemak Total", "Fat Total")
                            if (fatValue != "0") nutritionalData["Lemak"] = fatValue
                        }
                        line.contains("Karbohidrat", ignoreCase = true) ||
                                line.contains("Karbohit", ignoreCase = true) ||
                                line.contains("Karbohldrat", ignoreCase = true) -> {
                            val carboValue = extractValueWithContext(line, "Karbohidrat", "Karbohit", "Karbohldrat")
                            if (carboValue != "0") nutritionalData["Karbohidrat"] = carboValue
                        }
                        line.contains("Protein", ignoreCase = true) -> {
                            val proteinValue = extractValueWithContext(line, "Protein")
                            if (proteinValue != "0") nutritionalData["Protein"] = proteinValue
                        }
                        else -> {
                            // Extract and store standalone numeric values
                            val numericValue = extractNumericValue(line)
                            if (numericValue.isNotEmpty()) {
                                intermediateNumericValues.add(Pair(line, numericValue))
                            }
                        }
                    }
                }

                // Fallback and improve extraction if specific categories are missing
                if (nutritionalData.isEmpty()) {
                    result.putAll(improvedFallbackExtraction(intermediateNumericValues))
                } else {
                    result.putAll(nutritionalData)
                }

                result.entries.removeIf {
                    val value = it.value.toDoubleOrNull()
                    value == null || value < 0 || value > 1000
                }
            } catch (e: Exception) {
                Log.e("OCRRepository", "OCR failed", e)
            }

            Log.d("OCRRepository", "Extracted values: $result")
            return@withContext result
        }
    }

    private fun extractValueWithContext(text: String, vararg contexts: String): String {
        Log.d("NutritionExtractor", "Processing text: $text")

        val normalizedText = preprocessNutritionText(text)

        val patterns = listOf(
            Regex("""(${contexts.joinToString("|")})[:\s]*(\d+\.?\d*)\s*g?"""),
            Regex("""(\d+\.?\d*)\s*g?\s*(${contexts.joinToString("|")})""")
        )

        for (pattern in patterns) {
            val matchResult = pattern.find(normalizedText)
            matchResult?.let {
                val value = it.groupValues[2]
                if (value.toDoubleOrNull() != null) {
                    Log.d("NutritionExtractor", "Extracted value: $value")
                    return value
                }
            }
        }
        return "0"
    }

    private fun extractNumericValue(text: String): String {
        val numericPattern = Regex("""\b(\d+\.?\d*)\s*(mg|g|kg|%)?""")
        val matchResult = numericPattern.find(text)
        return matchResult?.groupValues?.get(1) ?: ""
    }

    private fun improvedFallbackExtraction(
        intermediateValues: List<Pair<String, String>>
    ): Map<String, String> {
        val sortedValues = intermediateValues
            .mapNotNull { (line, value) ->
                value.toDoubleOrNull()?.let { Triple(line, value, it) }
            }
            .sortedByDescending { it.third }

        val result = mutableMapOf<String, String>()

        // Prioritize lines with specific keywords
        val fatValue = sortedValues.find {
            it.first.contains("Lemak Total", ignoreCase = true) ||
                    it.first.contains("Lemak", ignoreCase = true)
        }?.second

        val proteinValue = sortedValues.find {
            it.first.contains("Protein", ignoreCase = true)
        }?.second

        val carboValue = sortedValues.find {
            it.first.contains("Karbohidrat", ignoreCase = true) ||
                    it.first.contains("Karbohit", ignoreCase = true) ||
                    it.first.contains("Karbohldrat", ignoreCase = true)
        }?.second

        // If specific values not found, use top 3 sorted values
        if (fatValue != null) result["Lemak"] = fatValue
        if (proteinValue != null) result["Protein"] = proteinValue
        if (carboValue != null) result["Karbohidrat"] = carboValue

        if (result.size < 3 && sortedValues.size >= 3) {
            result["Lemak"] = result["Lemak"] ?: sortedValues[0].second
            result["Protein"] = result["Protein"] ?: sortedValues[1].second
            result["Karbohidrat"] = result["Karbohidrat"] ?: sortedValues[2].second
        }

        return result
    }

    private fun preprocessNutritionText(text: String): String {
        return text.lowercase()
            .replace(",", ".")
            .replace("–", "-")
            .replace("total", "")
            .replace("[^a-z0-9.:=\\s%]".toRegex(), "")
            .trim()
    }
}