package com.example.nutripal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.data.repository.AuthRepository
import com.example.nutripal.ui.screen.HomeScreen
import com.example.nutripal.ui.screen.SplashScreen
import com.example.nutripal.ui.screen.auth.LoginScreen
import com.example.nutripal.ui.screen.auth.RegisterScreen
import com.example.nutripal.ui.screen.auth.VerificationScreen
import com.example.nutripal.ui.screen.onboarding.OnboardingScreen
import com.example.nutripal.ui.screen.profile.PersonalDetailsScreen1
import com.example.nutripal.ui.screen.profile.PersonalDetailsScreen2
import com.example.nutripal.viewmodel.LoginViewModel
import com.example.nutripal.viewmodel.OnboardingViewModel
import com.example.nutripal.viewmodel.PersonalDetailsViewModel
import com.example.nutripal.viewmodel.RegisterViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

// Define routes as sealed class for type safety
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object PersonalDetails1 : Screen("personal_details_1")
    object PersonalDetails2 : Screen("personal_details_screen_2")
    object EmailVerification : Screen("email_verification")
    object Home : Screen("home")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authRepository = AuthRepository(FirebaseAuth.getInstance())

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
            HomeScreen()
    }
    }
}