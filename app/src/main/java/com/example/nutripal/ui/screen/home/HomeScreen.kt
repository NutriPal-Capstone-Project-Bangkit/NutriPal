package com.example.nutripal.ui.screen.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.component.home.DailyNutritionCard
import com.example.nutripal.ui.component.home.HomeBottomNavigation
import com.example.nutripal.ui.component.home.HomeContent
import com.example.nutripal.ui.component.home.HomeHeader
import com.example.nutripal.ui.component.home.HomeStatusBar
import com.example.nutripal.ui.component.home.ScannerButton
import com.example.nutripal.ui.theme.Primary

@Composable
fun HomeScreen(navController: NavController, saveState: Boolean = false) {
    val context = LocalContext.current

    // Move BackHandler inside the composable function
    BackHandler {
        (context as? Activity)?.moveTaskToBack(true)
    }

    HomeStatusBar()

    val homeViewModel: HomeViewModel = hiltViewModel()
    val newsList by remember { homeViewModel.newsList }
    val isLoading by remember { homeViewModel.isLoading }
    val errorMessage by remember { homeViewModel.errorMessage }
    val currentRoute by remember { mutableStateOf("home") }

    LaunchedEffect(Unit) {
        homeViewModel.loadNews()
    }

    val backgroundModifier = remember {
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .background(Primary)
            .zIndex(0f)
    }

    val homeContent = remember(newsList, isLoading, errorMessage) {
        @Composable {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState())
                    .zIndex(1f)
            ) {
                HomeHeader()
                Spacer(modifier = Modifier.height(24.dp))
                DailyNutritionCard()
                Spacer(modifier = Modifier.height(24.dp))

                HomeContent(
                    navController = navController,
                    newsList = newsList,
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Background header
        Box(modifier = backgroundModifier)

        // Main content
        homeContent()

        // Scanner button
        ScannerButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 15.dp)
                .zIndex(2f),
            navController = navController,
            context = context,
        )

        // Bottom navigation
        HomeBottomNavigation(
            currentRoute = currentRoute,
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        )
    }
    LaunchedEffect(saveState) {
        if (!saveState) {
            homeViewModel.loadNews()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController)
}