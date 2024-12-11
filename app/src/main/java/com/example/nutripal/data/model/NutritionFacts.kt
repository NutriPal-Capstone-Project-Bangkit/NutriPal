package com.example.nutripal.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NutritionFacts(
    val carbohydrate: Int,
    val protein: Int,
    val fat: Int
) : Parcelable
