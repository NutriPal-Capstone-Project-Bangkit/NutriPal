package com.example.nutripal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nutripal.ui.navigation.AppNavigation
import com.example.nutripal.ui.theme.NutriPalTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(applicationContext)

        setContent {
            NutriPalTheme {
                AppNavigation()
            }
        }
    }
}
