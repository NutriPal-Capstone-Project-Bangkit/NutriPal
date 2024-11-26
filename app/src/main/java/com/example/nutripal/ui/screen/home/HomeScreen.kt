package com.example.nutripal.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.repository.NewsRepository
import com.example.nutripal.ui.component.home.*
import com.example.nutripal.ui.component.home.news.NewsItem
import com.example.nutripal.ui.theme.Primary
import com.example.nutripal.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController) {
    HomeStatusBar()

    var scannedText by remember { mutableStateOf("") }
    val homeViewModel: HomeViewModel = hiltViewModel()
    val newsList by homeViewModel.newsList
    val isLoading by homeViewModel.isLoading
    val errorMessage by homeViewModel.errorMessage
    var currentRoute by remember { mutableStateOf("home") }

    LaunchedEffect(Unit) {
        homeViewModel.loadNews()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Background header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .background(Primary)
                .zIndex(0f)
        )

        // Main content
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

        // Scanner button
        ScannerButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 15.dp)
                .zIndex(2f),
            onScanResult = { result ->
                scannedText = result
            }
        )

        Text(
            text = scannedText,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 16.sp
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
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(navController)
}