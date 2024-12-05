package com.example.nutripal.ui.screen.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var navigationTriggered: Boolean
        get() = savedStateHandle.get("navigationTriggered") ?: false
        set(value) {
            savedStateHandle["navigationTriggered"] = value
        }

    fun navigateToOnboarding(navController: NavController) {
        // Use synchronized block to prevent race conditions
        synchronized(this) {
            if (!navigationTriggered) {
                navigationTriggered = true
                viewModelScope.launch {
                    delay(2000)
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }
}