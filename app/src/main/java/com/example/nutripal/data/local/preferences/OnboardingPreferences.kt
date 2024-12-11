package com.example.nutripal.data.local.preferences

import android.content.Context
import android.content.SharedPreferences

class OnboardingPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", false)
    }

    fun setOnboardingCompleted() {
        sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
    }
}
