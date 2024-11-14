package com.example.nutripal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.theme.NutriPalTheme
import com.example.nutripal.ui.screen.PersonalDetailsScreen1
import com.example.nutripal.ui.screen.PersonalDetailsScreen2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriPalTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "personal_details_screen_1") {
                    composable("personal_details_screen_1") {
                        PersonalDetailsScreen1(navController = navController)
                    }
                    composable("personal_details_screen_2") {
                        PersonalDetailsScreen2(viewModel = viewModel(), onImageClick = { clickedItem ->
                            println("$clickedItem gambar diklik")
                        })
                    }
                }
            }
        }
    }
}