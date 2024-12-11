package com.example.nutripal.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutripal.data.local.NutritionDatabase
import com.example.nutripal.data.local.NutritionDatabase.Companion.getDatabase
import com.example.nutripal.data.local.dao.HistoryDao
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.repository.HistoryRepository
import com.example.nutripal.ui.component.home.ScannerButton
import com.example.nutripal.ui.screen.auth.login.LoginScreen
import com.example.nutripal.ui.screen.auth.login.LoginViewModel
import com.example.nutripal.ui.screen.auth.login.resetpassword.ForgotPasswordScreen
import com.example.nutripal.ui.screen.auth.login.resetpassword.ResetPasswordFailedScreen
import com.example.nutripal.ui.screen.auth.login.resetpassword.ResetPasswordSentScreen
import com.example.nutripal.ui.screen.auth.login.resetpassword.ResetPasswordSuccesfullScreen
import com.example.nutripal.ui.screen.auth.register.RegisterScreen
import com.example.nutripal.ui.screen.auth.register.RegisterViewModel
import com.example.nutripal.ui.screen.auth.register.VerificationScreen
import com.example.nutripal.ui.screen.home.HomeScreen
import com.example.nutripal.ui.screen.home.chatbot.ChatScreen
import com.example.nutripal.ui.screen.home.dailytrack.DailyNutritionTrackScreen
import com.example.nutripal.ui.screen.home.dailytrack.details.DailyTrackDetailsScreen
import com.example.nutripal.ui.screen.home.detailnews.NewsDetailScreen
import com.example.nutripal.ui.screen.onboarding.OnboardingScreen
import com.example.nutripal.ui.screen.onboarding.OnboardingViewModel
import com.example.nutripal.ui.screen.personaldetails.PersonalDetailsScreen1
import com.example.nutripal.ui.screen.personaldetails.PersonalDetailsScreen2
import com.example.nutripal.ui.screen.personaldetails.PersonalDetailsViewModel
import com.example.nutripal.ui.screen.personaldetails.saved.PersonalDetailsSavedScreen
import com.example.nutripal.ui.screen.profile.ProfileScreen
import com.example.nutripal.ui.screen.profile.about.AboutAppScreen
import com.example.nutripal.ui.screen.profile.edit.EditProfileScreen
import com.example.nutripal.ui.screen.profile.edit.EditProfileViewModel
import com.example.nutripal.ui.screen.profile.history.HistoryScanScreen
import com.example.nutripal.ui.screen.profile.history.HistoryScanViewModel
import com.example.nutripal.ui.screen.profile.history.details.HistoryScanDetailsViewModel
import com.example.nutripal.ui.screen.profile.settings.SettingsScreen
import com.example.nutripal.ui.screen.profile.settings.SettingsViewModel
import com.example.nutripal.ui.screen.profile.settings.change.email.ChangeEmailScreen
import com.example.nutripal.ui.screen.profile.settings.change.password.ChangePasswordScreen
import com.example.nutripal.ui.screen.scan.CropScreen
import com.example.nutripal.ui.screen.scan.camera.CameraScreen
import com.example.nutripal.ui.screen.profile.history.details.HistoryScanDetailsScreen
import com.example.nutripal.ui.screen.scan.result.ResultScreen
import com.example.nutripal.ui.screen.scan.result.savetodaily.AddToDailyNutritionScreen
import com.example.nutripal.ui.screen.scan.result.savetodaily.success.AddToDailySuccessScreen
import com.example.nutripal.ui.screen.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val onBackClick: () -> Unit = {}

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }


        composable(Screen.Onboarding.route) {
            OnboardingScreen(OnboardingViewModel(), navController, context)
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(viewModel = viewModel, navController = navController)
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController, onBackClick)
        }

        composable(
            route = Screen.ResetPasswordSent.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordSentScreen(
                email = email,
                navController,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Screen.ResetPasswordSuccesfull.route) {
            ResetPasswordSuccesfullScreen(navController)
        }

        composable(Screen.ResetPasswordFailed.route) {
            ResetPasswordFailedScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, context = context)
        }

        composable(Screen.PersonalDetails1.route) {
            PersonalDetailsScreen1(PersonalDetailsViewModel(), navController)
        }

        composable(
            "personal_details_screen_2?name={name}&gender={gender}&profilePicture={profilePicture}&height={height}&weight={weight}&age={age}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("gender") { type = NavType.StringType },
                navArgument("profilePicture") { type = NavType.StringType; nullable = true },
                navArgument("height") { type = NavType.StringType; nullable = true },
                navArgument("weight") { type = NavType.StringType; nullable = true },
                navArgument("age") { type = NavType.StringType; nullable = true },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val gender = backStackEntry.arguments?.getString("gender")
            val profilePicture = backStackEntry.arguments?.getString("profilePicture")
            val age = backStackEntry.arguments?.getString("age")
            val height = backStackEntry.arguments?.getString("height")
            val weight = backStackEntry.arguments?.getString("weight")
            PersonalDetailsScreen2(viewModel(), navController, name, gender, profilePicture, age, weight, height )
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
        composable(Screen.Profile.route) {
            ProfileScreen(navController, context)
        }

        composable("daily_track"){DailyNutritionTrackScreen(navController)}

        composable("daily_nutrition_success"){AddToDailySuccessScreen(navController)}

        composable(
            route = "news_detail/{encodedUrl}",
            arguments = listOf(
                navArgument("encodedUrl") {
                    type = NavType.StringType
                }
            )
        ) {
            NewsDetailScreen(navController = navController)
        }

        composable(
            route = "daily_track_details/{documentId}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")
            documentId?.let {
                DailyTrackDetailsScreen(
                    navController = navController,
                    documentId = it
                )
            }
        }

        composable(Screen.EditProfile.route) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid ?: ""

            val apiService = ApiConfig.getProfileApiService()

            val viewModel = EditProfileViewModel(apiService = apiService, uid = uid)

            EditProfileScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }

        composable("crop_screen") { CropScreen(navController) }

        composable(Screen.ResultScreen.route) {
            ResultScreen(
                navController = navController,
                nutritionFacts = NutritionFacts(30, 20, 20),
                recommendation = ""
            )
        }

        composable("change_email") { ChangeEmailScreen(navController) }

        composable("change_password") { ChangePasswordScreen(navController) }

        composable("settings") {
            SettingsScreen(
                onBackClick = {},
                navController,
                viewModel = SettingsViewModel()
            )
        }

        composable("camera_screen") { CameraScreen(navController) }

        composable(Screen.Scanner.route) {
            ScannerButton(
                modifier = Modifier,
                navController = navController,
                context = context,
            )
        }
        composable("personal_detail_saved") { PersonalDetailsSavedScreen(navController) }

        composable("history_scan") {

            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()

            val historyDao = getDatabase(context).historyDao()

            val historyRepository = HistoryRepository(firestore = firestore, historyDao = historyDao, auth = auth)

            val historyScanViewModel = HistoryScanViewModel(historyRepository = historyRepository)

            HistoryScanScreen(
                onBackClick, navController, historyScanViewModel)
        }

        composable("history_details/{documentId}") { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")
            val viewModel: HistoryScanDetailsViewModel = hiltViewModel()

            HistoryScanDetailsScreen(
                navController = navController,
                viewModel = viewModel,
                documentId = documentId ?: ""
            )
        }

        composable(Screen.AboutApp.route){
            AboutAppScreen(navController)
        }
        composable(
            route = "add_to_daily_nutrition_screen/{carbohydrate}/{protein}/{fat}/{recommendation}",
            arguments = listOf(
                navArgument("carbohydrate") { type = NavType.IntType },
                navArgument("protein") { type = NavType.IntType },
                navArgument("fat") { type = NavType.IntType },
                navArgument("recommendation") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "No recommendation available"
                }
            )
        ) { backStackEntry ->
            val carbohydrate = backStackEntry.arguments?.getInt("carbohydrate") ?: 0
            val protein = backStackEntry.arguments?.getInt("protein") ?: 0
            val fat = backStackEntry.arguments?.getInt("fat") ?: 0
            val recommendation = backStackEntry.arguments?.getString("recommendation")
                ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
                ?: "No recommendation available"

            val imageUri = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Uri>("cropped_image_uri")

            AddToDailyNutritionScreen(
                navController = navController,
                imageUri = imageUri,
                carbohydrate = carbohydrate,
                protein = protein,
                fat = fat,
                recommendation = recommendation
            )
        }
    }
}