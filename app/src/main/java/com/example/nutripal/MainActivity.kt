package com.example.nutripal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import com.example.nutripal.ui.theme.NutriPalTheme
import com.example.nutripal.ui.navigation.AppNavigation
import com.example.nutripal.ui.screen.PersonalDetailsScreen
import com.example.nutripal.ui.screen.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriPalTheme {
                AppNavigation()
            }
        }
    }
}
