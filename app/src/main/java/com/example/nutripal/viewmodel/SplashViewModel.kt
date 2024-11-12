package com.example.nutripal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    fun navigateToOnboarding(navController: NavController) {
        viewModelScope.launch {
            delay(2500)
            navController.navigate("onboarding")
        }
    }
}
