package com.example.nutripal.ui.navigation

import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutripal.ui.screen.home.ChatScreen
import com.example.nutripal.ui.screen.home.HomeScreen
import com.example.nutripal.ui.screen.SplashScreen
import com.example.nutripal.ui.screen.auth.LoginScreen
import com.example.nutripal.ui.screen.auth.RegisterScreen
import com.example.nutripal.ui.screen.auth.VerificationScreen
import com.example.nutripal.ui.screen.home.NewsDetailScreen
import com.example.nutripal.ui.screen.onboarding.OnboardingScreen
import com.example.nutripal.ui.screen.personaldetails.PersonalDetailsScreen1
import com.example.nutripal.ui.screen.personaldetails.PersonalDetailsScreen2
import com.example.nutripal.viewmodel.DetailNewsViewModel
import com.example.nutripal.viewmodel.LoginViewModel
import com.example.nutripal.viewmodel.OnboardingViewModel
import com.example.nutripal.viewmodel.PersonalDetailsViewModel
import com.example.nutripal.viewmodel.RegisterViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(OnboardingViewModel(), navController)
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.Register.route) {
            val context = LocalContext.current
            RegisterScreen(navController = navController, context = context)
        }

        composable(Screen.PersonalDetails1.route) {
            PersonalDetailsScreen1(PersonalDetailsViewModel(), navController)
        }

        composable(Screen.PersonalDetails2.route) {
            PersonalDetailsScreen2(viewModel = viewModel(), navController = navController)
        }


        composable(Screen.EmailVerification.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            VerificationScreen(
                navController = navController,
                viewModel = registerViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Chatbot.route) {
            ChatScreen(viewModel(), navController)
        }
        composable(
            route = "news_detail/{encodedUrl}", 
            arguments = listOf(
                navArgument("encodedUrl") {
                    type = NavType.StringType
                }
            )
        ) {
            val viewModel: DetailNewsViewModel = hiltViewModel()
            NewsDetailScreen(navController = navController)
        }
    }
}