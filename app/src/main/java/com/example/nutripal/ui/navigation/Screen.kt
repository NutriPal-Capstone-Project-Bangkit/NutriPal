package com.example.nutripal.ui.navigation

import android.util.Base64
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object PersonalDetails1 : Screen("personal_details_1")
    data object PersonalDetails2 : Screen("personal_details_screen_2")
    data object EmailVerification : Screen("email_verification")
    data object Home : Screen("home")
    data object Chatbot : Screen("chatbot")
    data object NewsDetail : Screen("news_detail/{encodedUrl}") {
        fun createRoute(url: String): String {
            val encodedUrl = Base64.encodeToString(url.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
            return "news_detail/$encodedUrl"
        }
    }
}