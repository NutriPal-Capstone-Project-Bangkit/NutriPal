@file:Suppress("FunctionName")

package com.example.nutripal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.screen.LoginScreen
import com.example.nutripal.ui.screen.OnboardingScreen
import com.example.nutripal.ui.screen.PersonalDetailsScreen1
import com.example.nutripal.ui.screen.PersonalDetailsScreen2
import com.example.nutripal.ui.screen.RegisterScreen
import com.example.nutripal.ui.screen.SplashScreen
import com.example.nutripal.viewmodel.LoginViewModel
import com.example.nutripal.viewmodel.OnboardingViewModel
import com.example.nutripal.viewmodel.PersonalDetailsViewModel
import com.example.nutripal.viewmodel.RegisterViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("onboarding") {
            OnboardingScreen(OnboardingViewModel(), navController)
        }
        composable("login") {
            LoginScreen(LoginViewModel(), navController)
        }
        composable("register") {
            RegisterScreen(RegisterViewModel(), navController)
        }
        composable("personal_details_1") {
            PersonalDetailsScreen1(PersonalDetailsViewModel(), navController)
        }
        composable("personal_details_screen_2") {
            PersonalDetailsScreen2(viewModel = viewModel(), onImageClick = { clickedItem ->
                println("$clickedItem gambar diklik")
            })
        }
    }
}

